package net.maslyna.post.integration;

import net.maslyna.post.integration.consumer.KafkaTestConsumer;
import net.maslyna.post.model.PostStatus;
import net.maslyna.post.model.dto.request.PostRequest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
public class PostIntegrationTests extends BasicIntegrationTest {
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
    public void createPostTests() throws Exception {
        createPost_returnsCreated();
        createRePost_returnsCreated();
    }

    @Test
    public void editPostTests() throws Exception {
        editPost_returnsOk();
        notValidUser_editPost_returnsForbidden();
    }

    @Test
    public void getAllPublishedPosts() throws Exception {
        long authUserId = 2L;
        PostRequest hiddenPost = PostRequest.builder()
                .title("test title")
                .text("test text")
                .hashtags(Set.of("test", "back-end", "not-boring-naming"))
                .status(PostStatus.HIDDEN)
                .build();
        for (int i = 0; i < 3; i++) {
            createPost(DEFAULT_USER, testPostRequest);
            createPost(DEFAULT_USER, hiddenPost);
        }
        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.get(ServiceURI.GET_ALL_PUBLISHED_POSTS)
                        .header(USER_HEADER, authUserId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("size", "20")
        ).andExpectAll(
                status().isOk()
        ).andReturn().getResponse();

        boolean isHiddenInResponse = response.getContentAsString().contains(PostStatus.HIDDEN.name());
        assertFalse(isHiddenInResponse);
    }

    private void createPost_returnsCreated() throws Exception {
        createPost(DEFAULT_USER, testPostRequest);
    }

    private void createRePost_returnsCreated() throws Exception {
        String content = jsonService.toJson(testPostRequest);
        MockHttpServletResponse post = createPost(DEFAULT_USER, testPostRequest);
        UUID postId = jsonService.extract(post.getContentAsString(), UUID.class);

        mockMvc.perform(
                MockMvcRequestBuilders.post(ServiceURI.CREATE_REPOST.formatted(postId))
                        .header(USER_HEADER, DEFAULT_USER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
        ).andExpectAll(
                status().isCreated(),
                content().contentType(MediaType.APPLICATION_JSON)
        );
    }

    private void editPost_returnsOk() throws Exception {
        String content = jsonService.toJson(testPostRequest);
        MockHttpServletResponse post = createPost(DEFAULT_USER, testPostRequest);
        UUID postId = jsonService.extract(post.getContentAsString(), UUID.class);

        mockMvc.perform(
                MockMvcRequestBuilders.put(ServiceURI.EDIT_POST.formatted(postId))
                        .header(USER_HEADER, DEFAULT_USER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
        ).andExpectAll(
                status().isOk(),
                content().contentType(MediaType.APPLICATION_JSON)
        );
    }

    private void notValidUser_editPost_returnsForbidden() throws Exception {
        long notValidUserId = 2L;
        String content = jsonService.toJson(testPostRequest);
        MockHttpServletResponse post = createPost(DEFAULT_USER, testPostRequest);
        UUID postId = jsonService.extract(post.getContentAsString(), UUID.class);

        mockMvc.perform(
                MockMvcRequestBuilders.put(ServiceURI.EDIT_POST.formatted(postId))
                        .header(USER_HEADER, notValidUserId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
        ).andExpectAll(
                status().isForbidden(),
                content().contentType(MediaType.APPLICATION_JSON)
        );
    }

    private MockHttpServletResponse createPost(long userId, PostRequest request) throws Exception {
        String content = jsonService.toJson(request);

        return mockMvc.perform(
                MockMvcRequestBuilders.post(ServiceURI.CREATE_POST)
                        .header(USER_HEADER, userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
        ).andExpectAll(
                status().isCreated(),
                content().contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();
    }
}
