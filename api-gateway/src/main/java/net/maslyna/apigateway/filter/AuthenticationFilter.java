package net.maslyna.apigateway.filter;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import net.maslyna.apigateway.exception.MissingAuthHeaderException;
import net.maslyna.apigateway.exception.ValidationRequestException;
import net.maslyna.apigateway.model.AccountResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.*;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@Component
@Slf4j
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {
    private final RouteValidator routeValidator;
    private final RestTemplate restTemplate;
    @Value("${jwt.prefix}")
    private String prefix;

    @Value("${jwt.validation.link}")
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
                String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

                if (authHeader == null || authHeader.isEmpty()) {
                    throw new MissingAuthHeaderException(HttpStatus.BAD_REQUEST, "missing auth header");
                }
                ResponseEntity<AccountResponse> response = sendValidationRequest(authHeader);

                if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                    ServerHttpRequest request = exchange.getRequest()
                            .mutate()
                            .header("userId", response.getBody().id().toString())
                            .header("username", response.getBody().email()).build();

                    return chain.filter(
                            exchange.mutate()
                                    .request(request)
                                    .build()
                    );
                } else {
                    throw new ValidationRequestException(HttpStatus.UNAUTHORIZED, "jwt token is not valid, status code {%s}"
                            .formatted(response.getStatusCode()));
                }
            }
            return chain.filter(exchange);
        };
    }

    private ResponseEntity<AccountResponse> sendValidationRequest(String authHeader) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, authHeader);

        RequestEntity<Void> request = RequestEntity.get(URI.create(jwtValidationLink))
                .headers(headers)
                .accept(MediaType.APPLICATION_JSON)
                .build();
        return restTemplate.exchange(request, AccountResponse.class);
    }

    private String extractJwt(String authHeader) {
        if (authHeader != null && authHeader.startsWith(prefix)) {
            return authHeader.substring(prefix.length());
        }
        return null;
    }

    public static class Config {
    }

    @PostConstruct
    private void logging() {
        log.info("jwtvalidationlink = {}", jwtValidationLink);
    }
}
