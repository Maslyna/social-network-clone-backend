package net.maslyna.user.integration.test;

import com.github.tomakehurst.wiremock.WireMockServer;
import lombok.extern.slf4j.Slf4j;
import net.maslyna.user.integration.config.MockFileServiceConfig;
import net.maslyna.user.integration.config.TestConfig;
import net.maslyna.user.integration.service.JsonService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static net.maslyna.user.integration.config.MockFileServiceConfig.*;
import static org.springframework.cloud.contract.wiremock.WireMockSpring.options;

@Slf4j

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@EnableConfigurationProperties
@Testcontainers(parallel = true)
@Import(value = {TestConfig.class, JsonService.class})

@Transactional
public class BasicIntegrationTest {
    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected JsonService jsonService;

    protected static final long DEFAULT_USER = 1L;
    protected static final String USER_HEADER = "userId";

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgreSQLContainer =
            new PostgreSQLContainer<>(DockerImageName.parse("postgres:latest"));

    protected static final WireMockServer mockFileServer = new WireMockServer(options().dynamicPort());
    protected static final WireMockServer mockSecurityServer = new WireMockServer(options().dynamicPort());

    @BeforeEach
    void prepare() {
        postgreSQLContainer.start();
    }

    @DynamicPropertySource
    static void propertySource(DynamicPropertyRegistry registry) {
        registry.add("eureka.client.file-service-url", mockFileServer::baseUrl);
        registry.add("eureka.client.security-service-url", mockSecurityServer::baseUrl);
    }

    @BeforeAll
    static void setUp() throws Exception {
        mockSecurityServer.start();
        mockFileServer.start();
    }

    @AfterAll
    static void setDown() {
        mockFileServer.stop();
        mockSecurityServer.stop();
    }
}
