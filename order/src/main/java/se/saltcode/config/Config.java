package se.saltcode.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

@Configuration
@EnableScheduling
public class Config {
  @Bean
  public WebClient webClientDev(@Value("${inventory.base-uri}") String baseUri) {
    return WebClient.create(baseUri);
  }
}
