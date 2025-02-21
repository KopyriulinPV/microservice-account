FROM openjdk:21-oracle

WORKDIR /app

COPY target/microservice-account-0.0.1-SNAPSHOT.jar app.jar

ENV SPRING_DATASOURCE_URL=jdbc:postgresql://postgres-docker-container/account_db
ENV SPRING_DATASOURCE_USERNAME=postgres
ENV SPRING_DATASOURCE_PASSWORD=1gjAVnJ
ENV TOKEN_VALIDATION_URL=http://auth-microservice/api/v1/auth/validate
ENV EVENTKAFKA_BOOTSTRAP-SERVERS=kafka-confluentinc-container:9092
ENV EUREKA_CLIENT_SERVICE-URL=http://gateway-eureka-microservice:8761/eureka/

EXPOSE 8082

ENTRYPOINT ["java", "-jar", "app.jar"]

