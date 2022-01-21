package com.example.gateway.filter;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;

//@Configuration
public class FilterConfig {
//    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
        return builder.routes(
        ).route(r -> r.path("/first-service/**")
                .filters(gatewayFilterSpec -> gatewayFilterSpec.addRequestHeader("first-service", "service-first-header-request")
                        .addResponseHeader("first-service", "service-first-header-response"))
                .uri("http://localhost:8081"))
                .route(r -> r.path("/second-service/**")
                        .filters(gatewayFilterSpec -> gatewayFilterSpec.addRequestHeader("second-service", "service-second-header-request")
                                .addResponseHeader("second-service", "service-second-header-response"))
                        .uri("http://localhost:8082")).
                        build();
    }
}
