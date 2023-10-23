package net.maslyna.follower.integration.tests;

import com.netflix.discovery.EurekaClient;
import lombok.extern.slf4j.Slf4j;
import net.maslyna.follower.client.UserClient;
import net.maslyna.follower.integration.service.JsonService;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.Map;

@Slf4j
@ActiveProfiles("test")
@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@EnableConfigurationProperties
@Testcontainers(parallel = true)
public class BasicIntegrationTest {
    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected JsonService jsonService;
    @Autowired
    protected UserClient userClient;
    @Autowired
    protected EurekaClient eurekaClient;

    @Container
    static KafkaContainer kafkaContainer =
            new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:latest"));

    @Container
    static GenericContainer<?> eurekaServer =
            new GenericContainer<>("springcloud/eureka")
                    .withExposedPorts(8761);

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:latest");

    @DynamicPropertySource
    static void containersProperties(DynamicPropertyRegistry registry) {
        registry.add("eureka.client.serviceUrl.defaultZone", () -> "http://localhost:8761/eureka");
    }

    public static final String USER_HEADER = "userId";
    public static final long DEFAULT_USER = 1L;

    @BeforeEach
    void prepare() {
        postgreSQLContainer.start();
        kafkaContainer.start();
    }
}
