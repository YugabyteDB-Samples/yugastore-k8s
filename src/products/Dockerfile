FROM eclipse-temurin:17-jre
COPY ./target/products-microservice-0.0.1-SNAPSHOT.jar /
EXPOSE 8082
CMD java -jar /products-microservice-0.0.1-SNAPSHOT.jar --server.port=8082 --eureka.instance.preferIpAddress=true --eureka.instance.hostname=products-microservice --cronos.yugabyte.hostname=$DB_HOSTNAME
