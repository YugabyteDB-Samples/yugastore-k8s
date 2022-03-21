FROM eclipse-temurin:17-jre
COPY ./target/checkout-microservice-0.0.1-SNAPSHOT.jar /
EXPOSE 8086
CMD java -jar /checkout-microservice-0.0.1-SNAPSHOT.jar --server.port=8086 --eureka.instance.preferIpAddress=true --eureka.instance.hostname=checkout-microservice --cronos.yugabyte.hostname=$DB_HOSTNAME
