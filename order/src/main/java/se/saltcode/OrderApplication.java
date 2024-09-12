package se.saltcode;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class OrderApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrderApplication.class, args);
	}

}
/*
api:
  base-path: /api
  controllers:
    orders: /order
    transactions: /transaction
server:
  port: 8080
spring:
  config:
    import: optional:file:.env[.properties]
  application:
    name: order
  datasource:
    username: ${DATABASE_USERNAME}
    password: ${DATABASE_PASSWORD}
  jpa:
    database-platform: org.hibernate.dialect.PostgreSQLDialect
    hibernate:
      ddl-auto: create-drop  #create-drop, create, validate etc
    show-sql: true  #Set to true to enable SQL logging
  cloud:
    gcp:
      sql:
        instance-connection-name: "${INSTANCE_CONNECTION_NAME}"
        database-name: ${DATABASE_NAME}
      credentials:
        location: ${GCP_AUTH_LOCATION}



 */