package net.maslyna.user.integration.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.matching.StringValuePattern;
import net.maslyna.user.client.response.FileStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class MockFileServiceConfig {

    private static final ResponseEntity<String> response = ResponseEntity.ok("TEST RESPONSE");
    private static final ResponseEntity<FileStatus> fileStatusResponse = ResponseEntity.ok(
            FileStatus.builder()
                    .status("TEST-STATUS")
                    .build()
    );
    private static final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());


    public static void configureSave(WireMockServer server) throws Exception {
        server.stubFor(post(urlMatching("/api/v1/file\\?contentId=.*&fileType=.*"))
                .willReturn(aResponse().withStatus(200)
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBody(mapper.writeValueAsString(response)))
        );
    }

    public static void configureDelete(WireMockServer server, UUID contentId) throws Exception {
        server.stubFor(delete(urlEqualTo("/api/v1/file/" + contentId))
                .withHeader("userId", matching("\\d+"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBody(mapper.writeValueAsString(fileStatusResponse)))
        );
    }

    public static void configureGet(WireMockServer server, UUID contentId) throws Exception {
        server.stubFor(get(urlEqualTo("/api/v1/file/" + contentId))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBody(mapper.writeValueAsString(response)))
        );
    }
}
