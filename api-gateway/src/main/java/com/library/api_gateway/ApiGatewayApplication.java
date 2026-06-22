package com.library.api_gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableDiscoveryClient
public class ApiGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiGatewayApplication.class, args);
	}

	@Bean
	public RouteLocator libraryRoutes(RouteLocatorBuilder builder) {
		return builder.routes()
				// 1. Catalog Service Route
				.route("catalog-service-route", r -> r
						.path("/books/**")
						.uri("lb://catalog-service"))

				// 2. User Service Route
				.route("user-service-route", r -> r
						.path("/users/**", "/login/**", "/api/users/**")
						.uri("lb://user-service"))

				// 3. Lending Service Route
				.route("lending-service-route", r -> r
						.path("/loans/**")
						.uri("lb://lending-service"))
				.build();
	}
}