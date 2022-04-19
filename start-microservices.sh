#!/bin/bash

DB_HOSTNAME=localhost
STORENUM=99


CLEAR='\033[0m'
RED='\033[0;31m'

function usage() {
  if [ -n "$1" ]; then
    echo -e "${RED}👉 $1${CLEAR}\n";
  fi
  echo "Usage: $0 [-host db-hostname] [-storenum storenum]"

  echo ""
  #echo "Example: $0 --number-of-people 2 --section-id 1 --cache-file last-known-date.txt"
  exit 1
}

# parse params
while [[ "$#" > 0 ]]; do case $1 in
  -host|--host) DB_HOSTNAME="$2"; shift;shift;;
  -storenum|--storenum) STORENUM="$2";shift;shift;;
  -v|--verbose) VERBOSE=1;shift;;
  *) usage "Unknown parameter passed: $1"; shift; shift;;
esac; done

echo "DB_HOSTNAME: $DB_HOSTNAME";
echo "STORENUM: $STORENUM";


export API_DIR=src
echo "API_DIR:$HOME"
export LOG_DIR=$HOME/Downloads/logs
#export STORENUM=99
#export DB_HOSTNAME=35.233.255.65

cd $API_DIR/target
java -jar -DSTORENUM=$STORENUM -DB_HOSTNAME=$DB_HOSTNAME api-gateway-microservice-0.0.1-SNAPSHOT.jar > $LOG_DIR/api.log &
java -jar -DSTORENUM=$STORENUM -DB_HOSTNAME=$DB_HOSTNAME eureka-server-local-0.0.1-SNAPSHOT.jar > $LOG_DIR/eureka.log &
java -jar -DSTORENUM=$STORENUM -DB_HOSTNAME=$DB_HOSTNAME products-microservice-0.0.1-SNAPSHOT.jar > $LOG_DIR/products.log &
java -jar -DSTORENUM=$STORENUM -DB_HOSTNAME=$DB_HOSTNAME cart-microservice-0.0.1-SNAPSHOT.jar > $LOG_DIR/cart.log &
java -jar -DSTORENUM=$STORENUM -DB_HOSTNAME=$DB_HOSTNAME checkout-microservice-0.0.1-SNAPSHOT.jar > $LOG_DIR/checkout.log &
java -jar -DSTORENUM=$STORENUM -DB_HOSTNAME=$DB_HOSTNAME react-ui-0.0.1-SNAPSHOT.jar > $LOG_DIR/react.log &
