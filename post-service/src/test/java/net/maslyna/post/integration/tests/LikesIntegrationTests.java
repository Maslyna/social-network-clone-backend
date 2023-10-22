package net.maslyna.post.integration.tests;

import net.maslyna.post.integration.uri.LikeURI;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class LikesIntegrationTests extends BasicIntegrationTest {

    private static final String DEFAULT_POST_ID = "e72861b1-c99d-47cc-9837-4421f5d166d5";
    private static final String DEFAULT_COMMENT_ID = "a88660db-8acb-4ca7-a8c5-27d2946a0316";

    @Test
    public void likePostTests() throws Exception {
        likePost_ReturnsCreated();
        likePostSecondTime_ReturnsConflict();
    }

    @Test
    public void likeCommentTests() throws Exception {
        likeComment_ReturnsCreated();
        likeCommentSecondTime_ReturnsConflict();
    }

    @Test
    public void getLikesOnPostTests() throws Exception {
        getLikesOnPost_ReturnsOk();
        getLikesOnPostWithNotValidParams_ReturnsBadRequest();
    }

    @Test
    public void getLikesOnCommentTests() throws Exception {
        getLikesOnComment_ReturnsOk();
        getLikesOnCommentWithNotValidParams_ReturnsBadRequest();
    }

    @Test
    public void deleteLikeFromPostTests() throws Exception {
        likePost_ReturnsCreated();
        deleteLikeFromPost_ReturnsOk();
        deleteNotExistedLikeFromPost_ReturnsNotFound();
    }

    @Test
    public void deleteLikeFromCommentTests() throws Exception {
        likeComment_ReturnsCreated();
        deleteLikeFromComment_ReturnsOk();
        deleteNotExistedLikeFromComment_ReturnsNotFound();
    }

    private void likePost_ReturnsCreated() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.post(LikeURI.LIKE_POST.formatted(DEFAULT_POST_ID))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(USER_HEADER, DEFAULT_USER)
        ).andExpectAll(
                status().isCreated()
        );
    }

    private void likePostSecondTime_ReturnsConflict() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.post(LikeURI.LIKE_POST.formatted(DEFAULT_POST_ID))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(USER_HEADER, DEFAULT_USER)
        ).andExpectAll(
                status().isConflict()
        );
    }

    private void likeComment_ReturnsCreated() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.post(LikeURI.LIKE_COMMENT.formatted(DEFAULT_COMMENT_ID))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(USER_HEADER, DEFAULT_USER)
        ).andExpectAll(
                status().isCreated()
        );
    }

    private void likeCommentSecondTime_ReturnsConflict() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.post(LikeURI.LIKE_COMMENT.formatted(DEFAULT_COMMENT_ID))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(USER_HEADER, DEFAULT_USER)
        ).andExpectAll(
                status().isConflict()
        );
    }

    private void getLikesOnPost_ReturnsOk() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get(LikeURI.GET_POSTS_LIKES.formatted(DEFAULT_POST_ID))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(USER_HEADER, DEFAULT_USER)
        ).andExpectAll(
                status().isOk()
        );
    }

    private void getLikesOnPostWithNotValidParams_ReturnsBadRequest() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get(LikeURI.GET_POSTS_LIKES.formatted(DEFAULT_POST_ID))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(USER_HEADER, DEFAULT_USER)
                        .param("size", "-1")
                        .param("page", "-1")
                        .param("orderBy", "-1")
                        .param("sortBy", "-1")
        ).andExpectAll(
                status().isBadRequest(),
                content().contentType(MediaType.APPLICATION_JSON)
        );
    }

    private void getLikesOnComment_ReturnsOk() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get(LikeURI.GET_COMMENTS_LIKES.formatted(DEFAULT_COMMENT_ID))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(USER_HEADER, DEFAULT_USER)
        ).andExpectAll(
                status().isOk()
        );
    }

    private void getLikesOnCommentWithNotValidParams_ReturnsBadRequest() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get(LikeURI.GET_COMMENTS_LIKES.formatted(DEFAULT_POST_ID))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(USER_HEADER, DEFAULT_USER)
                        .param("size", "-1")
                        .param("page", "-1")
                        .param("orderBy", "-1")
                        .param("sortBy", "-1")
        ).andExpectAll(
                status().isBadRequest(),
                content().contentType(MediaType.APPLICATION_JSON)
        );
    }

    private void deleteLikeFromPost_ReturnsOk() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.delete(LikeURI.DELETE_LIKE_ON_POST.formatted(DEFAULT_POST_ID))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(USER_HEADER, DEFAULT_USER)
        ).andExpectAll(
                status().isOk()
        );
    }

    private void deleteNotExistedLikeFromPost_ReturnsNotFound() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.delete(LikeURI.DELETE_LIKE_ON_POST.formatted(DEFAULT_POST_ID))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(USER_HEADER, DEFAULT_USER)
        ).andExpectAll(
                status().isNotFound()
        );
    }

    private void deleteLikeFromComment_ReturnsOk() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.delete(LikeURI.DELETE_LIKE_ON_COMMENT.formatted(DEFAULT_COMMENT_ID))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(USER_HEADER, DEFAULT_USER)
        ).andExpectAll(
                status().isOk()
        );
    }

    private void deleteNotExistedLikeFromComment_ReturnsNotFound() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.delete(LikeURI.DELETE_LIKE_ON_COMMENT.formatted(DEFAULT_COMMENT_ID))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(USER_HEADER, DEFAULT_USER)
        ).andExpectAll(
                status().isNotFound()
        );
    }

}
