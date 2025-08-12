package com.webstore.gateway_service.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@Component
public class JwtAuthFilter extends AbstractGatewayFilterFactory<JwtAuthFilter.Config> {

    @Value("${application.config.auth-url}")
    private String authUrl;
    private final WebClient.Builder webClientBuilder;

    public JwtAuthFilter(WebClient.Builder webClientBuilder) {
        super(Config.class);
        this.webClientBuilder = webClientBuilder;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {

            String accessToken = getAccessToken(exchange.getRequest());
            var refreshTokenCookie = exchange.getRequest().getCookies().getFirst("refresh-token");
            var csrfTokenCookie = exchange.getRequest().getCookies().getFirst("X-CSRF-TOKEN");

            if (accessToken == null || refreshTokenCookie == null || csrfTokenCookie == null) {
                return  unauthorized(exchange, "Tokens missing");
            }

            return webClientBuilder
                    .build()
                    .post()
                    .uri(authUrl + "/token-verify")
                    .header("Authorization", "Bearer " + accessToken)
                    .cookie("refresh-token", refreshTokenCookie.getValue())
                    .cookie("X-CSRF-TOKEN", csrfTokenCookie.getValue())
                    .exchangeToMono(clientResponse -> {
                       if (clientResponse.statusCode().isError()) {
                           return unauthorized(exchange, clientResponse.bodyToMono(String.class).block());
                       }
                       // add new access token if created;
                        var newAccessToken = clientResponse.headers().asHttpHeaders().getFirst("Authorization");
                        if (newAccessToken != null && !newAccessToken.isEmpty()) {
                            exchange.getRequest().mutate().header("Authorization", newAccessToken).build();
                        } else {
                            return unauthorized(exchange, clientResponse.bodyToMono(String.class).block());
                        }
                        return chain.filter(exchange);
                    });
        };
    }

    public static class Config {

    }

    private String getAccessToken(ServerHttpRequest request) {
        var accessToken = request.getHeaders().getFirst("Authorization");
        if (accessToken != null && accessToken.startsWith("Bearer ")) {
            return accessToken.substring(7);
        }
        return null;
    }

    private Mono<Void> unauthorized(ServerWebExchange exchange, String errorMessage) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        exchange.getResponse().getHeaders().add("Content-Type", "application/json");

        String responseBody = String.format("{\"error\": \"%s\"}", errorMessage);
        byte[] bytes = responseBody.getBytes(StandardCharsets.UTF_8);

        return exchange.getResponse()
                .writeWith(Mono.just(exchange.getResponse().bufferFactory().wrap(bytes)));
    }

}
