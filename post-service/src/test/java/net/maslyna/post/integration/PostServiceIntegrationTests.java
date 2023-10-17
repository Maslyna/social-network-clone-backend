package net.maslyna.post.integration;

import net.maslyna.post.integration.consumer.KafkaTestConsumer;
import net.maslyna.post.model.PostStatus;
import net.maslyna.post.model.dto.request.PostRequest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Set;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class PostServiceIntegrationTests extends BasicIntegrationTest {
    private KafkaTestConsumer consumer;

    private static final PostRequest testPostRequest =
            PostRequest.builder()
                    .title("test title")
                    .text("test text")
                    .hashtags(Set.of("test", "back-end", "not-boring-naming"))
                    .status(PostStatus.PUBLISHED)
                    .build();

    private static final String USER_HEADER = "userId";
    private static final long DEFAULT_USER = 1L;

    @Test
    public void createPost_returnsCreated() throws Exception {
        String content = objectMapper.writeValueAsString(testPostRequest);

        mockMvc.perform(
                MockMvcRequestBuilders.post(ServiceURI.CREATE_POST)
                        .header(USER_HEADER, DEFAULT_USER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
        ).andExpectAll(
                status().isCreated(),
                content().contentType(MediaType.APPLICATION_JSON)
        );
    }
}
