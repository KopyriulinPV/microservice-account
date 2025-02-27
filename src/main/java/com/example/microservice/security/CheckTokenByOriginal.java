package com.example.microservice.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@Slf4j
@RequiredArgsConstructor
public class CheckTokenByOriginal {

    private final WebClient checkTokenWebClient;

    public Boolean getStatusToken(String token) {
        if (!getStatusTokenByAuthApi(token)) {
            log.info("Не действительный токен авторизации {}", token);
            return false;
        }
        log.info("Действительный токен авторизации {}", token);
        return true;
    }

    private Boolean getStatusTokenByAuthApi(String token) {
        log.info("Проверка статуса токена {}", token);
        return checkTokenWebClient
                .get()
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .bodyToMono(Boolean.class)
                .block();
    }

    /*private final WebClient webClient;*/

    /*public Mono<Boolean> validateAuth(String token) {

        return webClient.post()
                .uri(uriBuilder -> uriBuilder.path("/api/v1/auth/validate").queryParam("token", token).build())
                .retrieve()
                .bodyToMono(Boolean.class);
    }*/
}



