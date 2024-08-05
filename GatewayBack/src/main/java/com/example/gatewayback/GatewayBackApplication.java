package com.example.gatewayback;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableDiscoveryClient
public class GatewayBackApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayBackApplication.class, args);
    }

    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("backendstage", r -> r.path("/api/v1/auth/**")
                        .uri("lb://backendstage"))
                .route("transferms", r -> r.path("/transfers/**")
                        .uri("lb://transferms"))
                .build();
    }

}
