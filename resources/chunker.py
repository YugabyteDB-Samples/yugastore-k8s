#!/usr/bin/env python3
# send chunks of csv data to postgres copy command

import sys
import io
import time
import datetime
import argparse
import psycopg2

# parse arguments

# all this because h is host instead of help
class MyParser(argparse.ArgumentParser):
    def error(self, message):
        sys.stderr.write('error: %s\n' % message)
        self.print_help()
        sys.exit(2)
    
parser = MyParser(formatter_class=argparse.ArgumentDefaultsHelpFormatter, add_help=False,
    description=__doc__)
parser.add_argument('--host', '-h', help='host', default='localhost')
parser.add_argument('--port', '-p', help='port', type=int, default=5433)
parser.add_argument('--database', '-d', help='database', required=True)
parser.add_argument('--table', '-t', help='table', required=True)
parser.add_argument('--user', '-u', help='user', default='yugabyte')
parser.add_argument('--password', help='password')
parser.add_argument('--chunksize', '-c', type=int, metavar='num_lines', help='number of lines in a chunk', default=1000)
parser.add_argument('--start-line', '-l', type=int, metavar='line_num', help="starting line number", default=1)
parser.add_argument('--end-line', '-e', type=int, metavar='line_num', help="ending line number")
parser.add_argument('--delimiter',  metavar='char', help="csv field delimiter", default=',')
parser.add_argument('--progress', type=int, metavar='N', help="print status update every N chunks", default=10)
parser.add_argument('filename', nargs='?', help='input csv file')
parser.add_argument('--help', help=argparse.SUPPRESS, action='store_true')
args = parser.parse_args()

# connect to the database

connection_string = "dbname=%s user=%s host=%s port=%s" % (args.database, args.user, args.host, args.port)
if args.password: connection_string += " password=%s" % args.password
try:
    db = psycopg2.connect(connection_string)
except psycopg2.OperationalError as e:
    raise
cursor = db.cursor()

# copy chunks into the table

def process_chunk(chunk):
    chunk.seek(0)
    try:
        #cursor.copy_from(chunk, args.table, sep=args.delimiter, null='') # cannot handle commas within fields
        cursor.copy_expert("COPY %s FROM STDIN WITH (FORMAT CSV)" % (args.table), chunk)
        db.commit()
    except psycopg2.errors.UniqueViolation as e:
        print(current_time(), "ERROR while processing chunk %s, last line read was %s, %s"
              % (chunk_number, i, sys.exc_info()[0]))
        cursor.execute("ROLLBACK")
    except (psycopg2.InternalError, psycopg2.OperationalError) as e:
        print(current_time(), "ERROR while processing chunk %s, last line read was %s, %s"
              % (chunk_number, i, sys.exc_info()[0]))
    if args.progress > 0 and chunk_number % args.progress == 0:
        print(current_time(), "Processed %s chunks so far, last line read was %s"
              % (chunk_number, i))

def current_time():
    return datetime.datetime.now().astimezone().strftime("%Y-%m-%d %H:%M:%S %Z")

chunksize = args.chunksize
chunk = io.StringIO() # needed for cursor's copy_from()
chunk_number = 1
starting_line = args.start_line
ending_line = args.end_line if args.end_line else 0

print(current_time(), "Beginning processing")
start_time = time.time()

reader = open(args.filename) if args.filename else sys.stdin
for i, line in enumerate(reader, start=1):
    if i < starting_line:
        continue
    chunk.write(line)
    if (i % chunksize == 0):
        process_chunk(chunk)
        del chunk
        chunk = io.StringIO()
        chunk_number += 1 # could be off by one if multiple of chunksize, but who cares
    if i == ending_line:
        break

# process the remainder
process_chunk(chunk)

end_time = time.time()
print(current_time(), "Processing complete in %s seconds.  %s total lines, %s chunks, %s lines per chunk."
      % (int(end_time-start_time), i-starting_line+1, chunk_number, chunksize))
