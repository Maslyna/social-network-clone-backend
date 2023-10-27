package net.maslyna.follower.integration.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import net.maslyna.common.response.UserResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.io.IOException;

public class UserMocks {
    private static final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());

    private static final UserResponse defaultResponse = UserResponse.builder()
            .id(1L)
            .email("test@mail.com")
            .name("test-user")
            .build();

    public static void setupMockBooksResponse(WireMockServer mockService) throws IOException {
        mockService.stubFor(
                WireMock.get(WireMock.urlMatching("/api/v1/user/[0-9]+"))
                        .willReturn(
                                WireMock.aResponse()
                                        .withStatus(HttpStatus.OK.value())
                                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                                        .withBody(mapper.writeValueAsString(defaultResponse))
                        )
        );
    }

}