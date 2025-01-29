FROM openjdk:21-oracle

WORKDIR /app

COPY target/microservice-account-0.0.1-SNAPSHOT.jar app.jar

ENV SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/account_db
ENV SPRING_DATASOURCE_USERNAME=postgres
ENV SPRING_DATASOURCE_PASSWORD=1gjAVnJ

EXPOSE 8082

ENTRYPOINT ["java", "-jar", "app.jar"]