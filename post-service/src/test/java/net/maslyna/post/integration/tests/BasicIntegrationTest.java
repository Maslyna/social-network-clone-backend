package net.maslyna.post.integration.tests;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.maslyna.post.integration.config.TestConfig;
import net.maslyna.post.integration.consumer.KafkaTestConsumer;
import net.maslyna.post.integration.service.JsonService;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@Import(value = {TestConfig.class, JsonService.class, KafkaTestConsumer.class})
@Testcontainers(parallel = true)
@ActiveProfiles("test")
public class BasicIntegrationTest {
    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected ObjectMapper objectMapper;
    @Autowired
    protected JsonService jsonService;
    @Autowired
    protected KafkaTestConsumer consumer;


    @Container
    static KafkaContainer kafkaContainer =
            new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:latest"));

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:latest");

    public static final String USER_HEADER = "userId";
    public static final long DEFAULT_USER = 1L;

    @BeforeEach
    void prepare() {
        postgreSQLContainer.start();
        kafkaContainer.start();
    }
}
