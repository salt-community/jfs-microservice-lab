package se.saltcode.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

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
}
