package net.maslyna.post.integration.tests;

import net.maslyna.post.integration.uri.PostURI;
import net.maslyna.post.model.PostStatus;
import net.maslyna.post.model.dto.request.PostRequest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class PostIntegrationTests extends BasicIntegrationTest {

    private static final PostRequest publicPost =
            PostRequest.builder()
                    .title("test title")
                    .text("test text")
                    .hashtags(Set.of("test", "back-end", "not-boring-naming"))
                    .status(PostStatus.PUBLISHED)
                    .build();

    private static final PostRequest hiddenPost = PostRequest.builder()
            .title("test title")
            .text("test text")
            .hashtags(Set.of("test", "back-end", "not-boring-naming"))
            .status(PostStatus.HIDDEN)
            .build();

    @Test
    public void createPostTests() throws Exception {
        boolean messageConsumed;

        createPost_returnsCreated();
        createRePost_returnsCreated();

        messageConsumed = consumer.getLatch().await(10, TimeUnit.SECONDS);
        assertTrue(messageConsumed);

        consumer.reset();

        messageConsumed = consumer.getLatch().await(10, TimeUnit.SECONDS);
        assertTrue(messageConsumed);

        consumer.reset();
    }

    @Test
    public void editPostTests() throws Exception {
        editPost_returnsOk();
        notValidUser_editPost_returnsForbidden();
        //TODO: validation tests
    }

    @Test
    public void getAllPublishedPostsTests() throws Exception {
        getAllPublishedPosts_ReturnsOk();
        //TODO: validation tests
    }

    @Test
    public void getAllPersonPostsTests() throws Exception {
        getAllPersonPosts_ReturnsOk();
        getAllPrivatePosts_ReturnsOk();
    }

    @Test
    public void getPostTests() throws Exception {
        final long authUserId = DEFAULT_USER + 1;

        final MockHttpServletResponse publicPostResponse = createPost(DEFAULT_USER, publicPost);
        final MockHttpServletResponse hiddenPostResponse = createPost(DEFAULT_USER, hiddenPost);
        final UUID publicPostId = jsonService.extract(publicPostResponse.getContentAsString(), UUID.class);
        final UUID hiddenPostId = jsonService.extract(hiddenPostResponse.getContentAsString(), UUID.class);

        getPost_ReturnsOk(authUserId, publicPostId);
        getPost_ReturnsOk(DEFAULT_USER, publicPostId);
        getPost_ReturnsOk(DEFAULT_USER, hiddenPostId);
        getPost_ReturnsForbidden(authUserId, hiddenPostId);
    }

    @Test
    public void deletePostTests() throws Exception {
        deletePost_ReturnsOk();
        deletePost_ReturnsForbidden();
        deletePost_ReturnsNotFound();
    }

    private void createPost_returnsCreated() throws Exception {
        createPost(DEFAULT_USER, publicPost);
    }

    private void createRePost_returnsCreated() throws Exception {
        final String content = jsonService.toJson(publicPost);
        final MockHttpServletResponse post = createPost(DEFAULT_USER, publicPost);
        final UUID postId = jsonService.extract(post.getContentAsString(), UUID.class);

        mockMvc.perform(
                MockMvcRequestBuilders.post(PostURI.CREATE_REPOST.formatted(postId))
                        .header(USER_HEADER, DEFAULT_USER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
        ).andExpectAll(
                status().isCreated(),
                content().contentType(MediaType.APPLICATION_JSON)
        );
    }

    private void editPost_returnsOk() throws Exception {
        final String content = jsonService.toJson(publicPost);
        final MockHttpServletResponse post = createPost(DEFAULT_USER, publicPost);
        final UUID postId = jsonService.extract(post.getContentAsString(), UUID.class);

        mockMvc.perform(
                MockMvcRequestBuilders.put(PostURI.EDIT_POST.formatted(postId))
                        .header(USER_HEADER, DEFAULT_USER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
        ).andExpectAll(
                status().isOk(),
                content().contentType(MediaType.APPLICATION_JSON)
        );
    }

    private void notValidUser_editPost_returnsForbidden() throws Exception {
        final long notValidUserId = 2L;
        final String content = jsonService.toJson(publicPost);
        final MockHttpServletResponse post = createPost(DEFAULT_USER, publicPost);
        final UUID postId = jsonService.extract(post.getContentAsString(), UUID.class);

        mockMvc.perform(
                MockMvcRequestBuilders.put(PostURI.EDIT_POST.formatted(postId))
                        .header(USER_HEADER, notValidUserId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
        ).andExpectAll(
                status().isForbidden(),
                content().contentType(MediaType.APPLICATION_JSON)
        );
    }

    private MockHttpServletResponse createPost(final long userId, final PostRequest request) throws Exception {
        final String content = jsonService.toJson(request);

        return mockMvc.perform(
                MockMvcRequestBuilders.post(PostURI.CREATE_POST)
                        .header(USER_HEADER, userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
        ).andExpectAll(
                status().isCreated(),
                content().contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();
    }

    private void getAllPublishedPosts_ReturnsOk() throws Exception {
        final long authUserId = DEFAULT_USER + 1;

        for (int i = 0; i < 3; i++) {
            createPost(DEFAULT_USER, publicPost);
            createPost(DEFAULT_USER, hiddenPost);
        }

        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.get(PostURI.GET_ALL_PUBLISHED_POSTS)
                        .header(USER_HEADER, authUserId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("size", "20")
        ).andExpectAll(
                status().isOk()
        ).andReturn().getResponse();

        boolean isHiddenInResponse = response.getContentAsString().contains(PostStatus.HIDDEN.name());
        assertFalse(isHiddenInResponse);
    }

    private void getAllPersonPosts_ReturnsOk() throws Exception {
        final long authUserId = DEFAULT_USER + 1;

        for (int i = 0; i < 3; i++) {
            createPost(DEFAULT_USER, publicPost);
            createPost(DEFAULT_USER, hiddenPost);
        }

        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.get(PostURI.GET_ALL_PERSONS_POSTS.formatted(DEFAULT_USER))
                        .header(USER_HEADER, authUserId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("size", "20")
        ).andExpectAll(
                status().isOk()
        ).andReturn().getResponse();

        boolean isHiddenInResponse = response.getContentAsString().contains(PostStatus.HIDDEN.name());
        assertFalse(isHiddenInResponse);
    }

    private void getAllPrivatePosts_ReturnsOk() throws Exception {
        for (int i = 0; i < 3; i++) {
            createPost(DEFAULT_USER, publicPost);
            createPost(DEFAULT_USER, hiddenPost);
        }

        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.get(PostURI.GET_ALL_PERSONS_POSTS.formatted(DEFAULT_USER))
                        .header(USER_HEADER, DEFAULT_USER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("size", "20")
        ).andExpectAll(
                status().isOk()
        ).andReturn().getResponse();

        boolean isHiddenInResponse = response.getContentAsString().contains(PostStatus.HIDDEN.name());
        assertTrue(isHiddenInResponse);
    }

    private void getPost_ReturnsOk(final long authUserId, final UUID postId) throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get(PostURI.GET_POST.formatted(postId))
                        .header(USER_HEADER, authUserId)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isOk()
        );
    }

    private void getPost_ReturnsForbidden(final long authUserId, final UUID postId) throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get(PostURI.GET_POST.formatted(postId))
                        .header(USER_HEADER, authUserId)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isForbidden()
        );
    }

    private void deletePost_ReturnsOk() throws Exception {
        final MockHttpServletResponse post = createPost(DEFAULT_USER, publicPost);
        final UUID postId = jsonService.extract(post.getContentAsString(), UUID.class);

        mockMvc.perform(
                MockMvcRequestBuilders.delete(PostURI.DELETE_POST.formatted(postId))
                        .header(USER_HEADER, DEFAULT_USER)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isOk()
        );
    }

    private void deletePost_ReturnsNotFound() throws Exception {
        final UUID postId = UUID.randomUUID();

        mockMvc.perform(
                MockMvcRequestBuilders.delete(PostURI.DELETE_POST.formatted(postId))
                        .header(USER_HEADER, DEFAULT_USER)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isNotFound()
        );
    }

    private void deletePost_ReturnsForbidden() throws Exception {
        final long authUserId = DEFAULT_USER + 1;
        final MockHttpServletResponse post = createPost(DEFAULT_USER, publicPost);
        final UUID postId = jsonService.extract(post.getContentAsString(), UUID.class);

        mockMvc.perform(
                MockMvcRequestBuilders.delete(PostURI.DELETE_POST.formatted(postId))
                        .header(USER_HEADER, authUserId)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isForbidden()
        );
    }
}
