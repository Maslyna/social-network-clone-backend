package net.maslyna.apigateway.config;

import net.maslyna.apigateway.filter.AuthenticationFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApiGatewayConfig {
    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder rlb, AuthenticationFilter authenticationFilter) {
        return rlb.routes()
                .route(p -> p.path("/security-service/api/v1/test")
                        .filters(f -> f.filter(authenticationFilter.apply(new AuthenticationFilter.Config())))
                        .uri("lb://SECURITY-SERVICE"))
                .build();
    }
}
