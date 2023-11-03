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
    @Value("${services.post-service}")
    private String POST_SERVICE;
    @Value("${services.follower-service}")
    private String FOLLOWER_SERVICE;

    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder rlb, AuthenticationFilter authenticationFilter) {
        return rlb.routes()
                // SECURITY-SERVICE routes
                .route(p -> p.path("/security-service/api-docs/**")
                        .filters(f -> f.rewritePath("/security-service/(?<segment>.*)", "/${segment}"))
                        .uri(SECURITY_SERVICE))
                .route(p -> p.path("/security-service/**")
                        .filters(f -> f.rewritePath("/security-service/(?<segment>.*)", "/${segment}"))
                        .uri(SECURITY_SERVICE))

                // USER-SERVICE routes
                .route(p -> p.path("/user-service/api-docs/**")
                        .filters(f ->
                                f.rewritePath("/user-service/(?<segment>.*)", "/${segment}"))
                        .uri(USER_SERVICE))
                .route(p -> p.path("/user-service/api/v1/user")
                        .filters(f -> f
                                .rewritePath("/user-service/(?<segment>.*)", "/${segment}"))
                        .uri(USER_SERVICE))
                .route(p -> p.path("/user-service/api/v1/user/**")
                        .filters(f -> f
                                .filter(authenticationFilter.apply(new AuthenticationFilter.Config()))
                                .rewritePath("/user-service/(?<segment>.*)", "/${segment}"))
                        .uri(USER_SERVICE))

                //POST-SERVICE routes
                .route(p -> p.path("/post-service/api-docs/**")
                        .filters(f -> f.rewritePath("/post-service/(?<segment>.*)", "/${segment}"))
                        .uri(POST_SERVICE))
                .route(p -> p.path("/post-service/api/v1/post")
                        .filters(f -> f.rewritePath("/post-service/(?<segment>.*)", "/${segment}"))
                        .uri(POST_SERVICE))
                .route(p -> p.path("/post-service/api/v1/post/**")
                        .filters(f -> f
                                .filter(authenticationFilter.apply(new AuthenticationFilter.Config()))
                                .rewritePath("/post-service/(?<segment>.*)", "/${segment}"))
                        .uri(POST_SERVICE))

                //FOLLOWER-SERVICE routes
                .route(p -> p.path("/follower-service/api-docs/**")
                        .filters(f -> f.rewritePath("/follower-service/(?<segment>.*)", "/${segment}"))
                        .uri(FOLLOWER_SERVICE))
                .route(p -> p.path("/follower-service/user/**")
                        .filters(f -> f.filter(authenticationFilter.apply(new AuthenticationFilter.Config()))
                                .rewritePath("/follower-service/(?<segment>.*)", "/${segment}"))
                        .uri(FOLLOWER_SERVICE))
                .build();
    }
}

