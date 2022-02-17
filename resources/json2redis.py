#!/usr/bin/env python3
# bulk load JSON documents into RediSearch
# USAGE: $0 files...

import sys
import fileinput
import json
import redis

INDEX = "ft"
BATCH_SIZE = 100
PORT=6380

r = redis.Redis(host="localhost", port=PORT)

# create the index
try:
    r.execute_command('FT.CREATE ft on HASH PREFIX 1 doc: SCHEMA sku TEXT NOSTEM  description TEXT  title TEXT  categories TAG')
except (ConnectionRefusedError, redis.exceptions.ConnectionError):
    sys.exit("cannot connect to redis")
except redis.exceptions.ResponseError:
    pass # index already exists

pipe = r.pipeline()
i = 0 # batch iterator

# parse files or stdin
for line in fileinput.input(sys.argv[1:]):
    i = i + 1
    ##print(i)
    ##print(line)
    line = line.rstrip()
    doc = json.loads(line)

    # sanitize and simplify the dictionary since the JSON document could be complex
    document = { k: doc[k] for k in { 'sku', 'description', 'title' } if k in doc }
    if 'categories' in doc:
        document['categories'] = ','.join(doc['categories'][0]) # convert list to string
    ##print(document)

    key = 'doc:' + document['sku']
    pipe.hset(key, mapping=document)
    if i % BATCH_SIZE == 0:
        pipe.execute()

if i % BATCH_SIZE != 0:
    pipe.execute() # last one

sys.stderr.write("processed " + str(i) + " lines\n")
