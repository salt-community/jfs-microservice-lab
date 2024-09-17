package se.saltcode.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class Config {

  @Value("${api.base-path}${api.controllers.orders}")
  private String contextPath;

  @Bean
  @Profile({"dev"})
  public String applicationUriDev(@Value("${server.port}") String port) {
    return "http://localhost:" + port + contextPath;
  }

  @Bean
  @Profile({"prod"})
  public String applicationUriProd() {
    return "https://order-983583191360.europe-west1.run.app" + contextPath;
  }

  @Bean
  @Profile({"dev"})
  public WebClient webClientDev() {
    return WebClient.create("http://localhost:5000/api/inventory/");
  }

  @Bean
  @Profile({"prod"})
  public WebClient webClientProd() {
    return WebClient.create("https://inventory-983583191360.europe-west1.run.app/api/inventory/");
  }
}
