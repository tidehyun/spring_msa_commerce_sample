package com.example.gateway.filter;

import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Component
@Slf4j
public class AuthorizationHeaderFilter extends AbstractGatewayFilterFactory<AuthorizationHeaderFilter.Config> {

    public AuthorizationHeaderFilter() {
        super(AuthorizationHeaderFilter.Config.class);
    }

    @Value("${token.secret}")
    private String secret;

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest serverHttpRequest = exchange.getRequest();
            if (serverHttpRequest.getMethod().matches(HttpMethod.POST.name())
                    && (serverHttpRequest.getURI().getPath().matches("/login") ||
                    serverHttpRequest.getURI().getPath().matches("/user"))) {
                return chain.filter(exchange);
            }
            if (serverHttpRequest.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                String authorizationHeader = serverHttpRequest.getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
                String jwt = authorizationHeader.replace("Bearer", "");
                if (isValidToken(jwt)) {
                    return chain.filter(exchange);
                } else {
                    return onError(exchange, "Invalid Token", HttpStatus.UNAUTHORIZED);
                }
            } else {
                return onError(exchange, "No Authorization header", HttpStatus.UNAUTHORIZED);
            }
        };
    }

    private boolean isValidToken(String jwt) {
        try {
            String subject = Jwts.parser().setSigningKey(secret)
                    .parseClaimsJws(jwt).getBody().getSubject();
            if (Objects.nonNull(subject)) {
                return Boolean.TRUE;
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
        return Boolean.FALSE;
    }

    private Mono<Void> onError(ServerWebExchange exchange, String errMsg, HttpStatus status) {
        log.error("error msg : {} , code : {}", errMsg, status);
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(status);
        return response.setComplete();
    }

    public static class Config {

    }
}
