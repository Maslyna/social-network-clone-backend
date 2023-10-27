package net.maslyna.follower.integration.tests;

import com.github.tomakehurst.wiremock.WireMockServer;
import lombok.extern.slf4j.Slf4j;
import net.maslyna.follower.integration.config.TestConfig;
import net.maslyna.follower.integration.config.UserMocks;
import net.maslyna.follower.integration.consumer.TestKafkaConsumer;
import net.maslyna.follower.integration.producer.TestKafkaProducer;
import net.maslyna.follower.integration.service.JsonService;
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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.io.IOException;

import static org.springframework.cloud.contract.wiremock.WireMockSpring.options;

@Slf4j

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@EnableConfigurationProperties
@ContextConfiguration(classes = TestConfig.class)
@Import(value = {JsonService.class, TestKafkaProducer.class, TestKafkaConsumer.class})
@Testcontainers(parallel = true)

@Transactional
public class BasicIntegrationTest {
    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected JsonService jsonService;
    @Autowired
    protected TestKafkaProducer testKafkaProducer;
    @Autowired
    protected TestKafkaConsumer testKafkaConsumer;

    @Container
    static KafkaContainer kafkaContainer =
            new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:latest"));
    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgreSQLContainer =
            new PostgreSQLContainer<>(DockerImageName.parse("postgres:latest"));

    static WireMockServer mockServer = new WireMockServer(options().dynamicPort());

    @DynamicPropertySource
    static void dynamicProperties(DynamicPropertyRegistry registry) {
        registry.add("eureka.client.user-service-url", () -> mockServer.baseUrl());
    }

    public static final String USER_HEADER = "userId";
    public static final long DEFAULT_USER = 1L;

    @BeforeEach
    void prepare() {
        postgreSQLContainer.start();
        kafkaContainer.start();
    }

    @BeforeAll
    static void setUp() throws IOException {
        mockServer.start();
        UserMocks.setupMockBooksResponse(mockServer);
    }

    @AfterAll
    static void setDown() {
        mockServer.stop();
    }
}
