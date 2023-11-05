package net.maslyna.user.integration.config;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class MockSecurityServiceConfig {

    public static void configureSecurityRegistration(WireMockServer server) {
        server.stubFor(post(urlEqualTo("/api/v1/security/register"))
                .withRequestBody(matchingJsonPath("$.email"))
                .withRequestBody(matchingJsonPath("$.password"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBody("{\"token\": \"Your-Authentication-Token\"}"))
        );
    }
}
