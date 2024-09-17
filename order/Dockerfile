FROM ubuntu:latest AS build
ENV DB_USERNAME=${DB_USERNAME}
ENV DB_PASSWORD=${DB_PASSWORD}
ENV DB_URL=${DB_URL}
RUN apt-get update && apt-get install -y maven
RUN apt-get update
RUN apt-get install openjdk-21-jdk -y
COPY . .
RUN mvn clean package -DskipTests
FROM eclipse-temurin:21
EXPOSE 8080
RUN mkdir /app
COPY --from=build /target/*.jar /app/order.jar
ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-jar","/app/order.jar"]

