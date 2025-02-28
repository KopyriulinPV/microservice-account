package com.example.microservice.security.jwt;

import com.example.microservice.security.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.util.Base64;


@RequiredArgsConstructor
@Slf4j
@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    private final UserDetailsServiceImpl userDetailsService;

    private final WebClient webClient;

    @Value("${app.integration.auth-url}")
    private String baseUrlForCheckRequestToken;



    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        log.info("00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000");
        log.info("вошел в JwtTokenFilter");


        try {
            String token = getToken(request);
            /*validateTokenWithAuthService(token)*/
            if(token != null && true) {

                String[] parts = token.split("\\.");

                String payload = parts[1];
                byte[] decodedBytes = Base64.getUrlDecoder().decode(payload);
                String decodedPayload = new String(decodedBytes);

                JSONObject json = new JSONObject(decodedPayload);

                String id = json.getString("sub");

                UserDetails userDetails = userDetailsService.loadUserByUsername(id);

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails,
                        null, userDetails.getAuthorities()
                );

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

        } catch (IllegalArgumentException e) {
            log.error("Невалидный JWT токен:", e);
        }
        filterChain.doFilter(request, response);
    }

    public boolean validateTokenWithAuthService(String token) {
        try {
            String authServiceUrl = String.format("%s%s", baseUrlForCheckRequestToken, token);
            // Отправляем запрос для проверки токена
            Boolean response = webClient.post()
                    .uri(authServiceUrl)
                    .header("Authorization", "Bearer " + token) // Если сервис требует токен в заголовке
                    .retrieve()
                    .bodyToMono(Boolean.class)
                    .block(); // Синхронное выполнение запроса

            return response != null && response;
        } catch (Exception e) {
            return false; // Если произошла ошибка, токен считается невалидным
        }
    }


    private String getToken(HttpServletRequest request) {
        String headerAuth = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }
        return null;
    }
}
