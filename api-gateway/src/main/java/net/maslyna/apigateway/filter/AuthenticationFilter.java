package net.maslyna.apigateway.filter;

import net.maslyna.apigateway.exception.MissingAuthHeaderException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

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

                try {
                    //TODO: REST call AUTH service
                    //TODO: refactor
                    HttpHeaders headers = new HttpHeaders();
                    String jwt = exchange.getRequest().getHeaders().getFirst("Authorization");
                    headers.set("Authorization", jwt);
                    RequestEntity<Void> request = new RequestEntity<>(headers, HttpMethod.GET, URI.create(jwtValidationLink));

                    ResponseEntity<Boolean> response = restTemplate.exchange(request, Boolean.class);

                    if (response.getStatusCode().is2xxSuccessful()
                            && response.getBody() != null && response.getBody()) {
                        return chain.filter(exchange);
                    }
                } catch (Exception e) {
                    throw new RuntimeException("REST call AUTH service troubles"); //TODO: custom exception
                }
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
