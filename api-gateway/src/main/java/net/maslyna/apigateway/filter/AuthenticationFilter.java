package net.maslyna.apigateway.filter;

import net.maslyna.apigateway.exception.MissingAuthHeaderException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {
    private final RouteValidator routeValidator;
    private final RestTemplate restTemplate;

    @Value("jwt.validation.link")
    private String jwtValidationLink;

    @Autowired
    public AuthenticationFilter(RouteValidator routeValidator, RestTemplate restTemplate) {
        super(Config.class);
        this.routeValidator = routeValidator;
        this.restTemplate = restTemplate;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            if (routeValidator.isSecured.test(exchange.getRequest())) {
                if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                    throw new MissingAuthHeaderException("missing auth header"); //TODO: custom exception
                }

                String jwt = extractJwt(exchange.getRequest().getHeaders());

            }
            return chain.filter(exchange);
        };
    }

    private String extractJwt(HttpHeaders headers) {
        String jwt = headers.getFirst(HttpHeaders.AUTHORIZATION);
        if (jwt != null && jwt.startsWith("Bearer ")) {
            jwt = jwt.substring(7);
        }
        return jwt;
    }

    public static class Config {
    }
}
