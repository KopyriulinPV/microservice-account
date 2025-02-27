package com.example.microservice.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class ClientConfiguration {

    @Bean
    public WebClient webClient(WebClient.Builder builder) {
        return builder.build();
    }

    /*@Bean
    public WebClient webClient(@Value("${app.integration.auth-url}") String authUrl) {
        return WebClient.builder()
                .baseUrl(authUrl)
                .build();
    }*/

  /*  @Bean
    public WebClient checkTokenWebClient() {
        return WebClient
                .builder()
                .baseUrl(baseUrlForCheckRequestToken)
                .build();
    }*/
}
