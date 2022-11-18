#!/bin/bash
export API_DIR=$HOME/gitsandbox/yugastore-k8s/src
echo "API_DIR:$HOME"
cd $API_DIR/api-gateway
./mvnw clean package -DskipTests
cp target/*.jar $API_DIR/target

cd $API_DIR/eureka-server-local
./mvnw clean package -DskipTests
cp target/*.jar $API_DIR/target

cd $API_DIR/products
./mvnw clean package -DskipTests
cp target/*.jar $API_DIR/target

cd $API_DIR/cart
./mvnw clean package -DskipTests
cp target/*.jar $API_DIR/target

cd $API_DIR/checkout
./mvnw clean package -DskipTests
cp target/*.jar $API_DIR/target

cd $API_DIR/react-ui
./mvnw clean package -DskipTests
cp target/*.jar $API_DIR/target
