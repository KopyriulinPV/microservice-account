package com.microservice.client;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class WebClientSender {

    private final WebClient webClient;

    public Mono<Boolean> validateAuth(String token) {

        return webClient.post()
                .uri(uriBuilder -> uriBuilder.path("/api/v1/auth/validate").queryParam("token", token).build())
                .retrieve()
                .bodyToMono(Boolean.class);

    }
}



