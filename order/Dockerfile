FROM ubuntu:latest AS build
RUN apt-get update
RUN apt-get install openjdk-21-jdk -y
COPY . .
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21

EXPOSE 8080

RUN mkdir /app
COPY --from=build /target/*.jar /app/order.jar
ENTRYPOINT ["java", "-jar","/app/order.jar"]

