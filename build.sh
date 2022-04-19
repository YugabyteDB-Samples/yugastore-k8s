#!/bin/bash
export API_DIR=$HOME/gitsandbox/yugastore-k8s/src
echo "API_DIR:$HOME"
cd $API_DIR/api-gateway
mvn clean package -DskipTests
cp target/*.jar $API_DIR/target

cd $API_DIR/eureka-server-local
mvn clean package -DskipTests
cp target/*.jar $API_DIR/target

cd $API_DIR/products
mvn clean package -DskipTests
cp target/*.jar $API_DIR/target

cd $API_DIR/cart
mvn clean package -DskipTests
cp target/*.jar $API_DIR/target

cd $API_DIR/checkout
mvn clean package -DskipTests
cp target/*.jar $API_DIR/target

cd $API_DIR/react-ui
mvn clean package -DskipTests
cp target/*.jar $API_DIR/target
