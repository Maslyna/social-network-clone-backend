package net.maslyna.apigateway.config;

import lombok.extern.slf4j.Slf4j;
import net.maslyna.apigateway.filter.AuthenticationFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@Slf4j
public class ApiGatewayConfig {
    @Value("${services.user-service}")
    private String USER_SERVICE;
    @Value("${services.security-service}")
    private String SECURITY_SERVICE;

    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder rlb, AuthenticationFilter authenticationFilter) {
        return rlb.routes()
                .route(p -> p.path("/user-service/api/v1/user")
                        .filters(f -> f
                                .rewritePath("/user-service/(?<segment>.*)", "/${segment}"))
                        .uri(USER_SERVICE))
                .route(p -> p.path("/user-service/api/v1/user/*")
                        .filters(f -> f
                                .filter(authenticationFilter.apply(new AuthenticationFilter.Config()))
                                .rewritePath("/user-service/(?<segment>.*)", "/${segment}"))
                        .uri(USER_SERVICE))
                .build();
    }
}

