package net.maslyna.post.integration.tests;

import net.maslyna.post.integration.uri.CommentURI;
import net.maslyna.post.model.dto.request.CommentRequest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.UUID;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class CommentIntegrationTests extends BasicIntegrationTest {

    private static final String DEFAULT_POST_ID = "e72861b1-c99d-47cc-9837-4421f5d166d5";

    private static final CommentRequest DEFAULT_COMMENT = CommentRequest.builder()
            .text("default text for comment")
            .build();

    @Test
    public void createCommentTests() throws Exception {
        createComment_ReturnsOk();
        createCommentOnNotRealPost_ReturnsNotFound();
        createEmptyComment_ReturnsBadRequest();
        createNotValidComment_ReturnsBadRequest();
    }

    @Test
    public void deleteCommentTests() throws Exception {
        deleteComment_ReturnsOk();
        deleteAnotherUserComment_ReturnsForbidden();
        deleteNotExistsComment_ReturnsNotFound();
    }

    private void createComment_ReturnsOk() throws Exception {
        createComment(DEFAULT_USER, DEFAULT_COMMENT);
    }

    private void createEmptyComment_ReturnsBadRequest() throws Exception {
        final CommentRequest request = new CommentRequest(null);

        mockMvc.perform(
                MockMvcRequestBuilders.post(CommentURI.CREATE_COMMENT.formatted(DEFAULT_POST_ID))
                        .header(USER_HEADER, DEFAULT_USER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonService.toJson(request))
        ).andExpectAll(
                status().isBadRequest(),
                content().contentType(MediaType.APPLICATION_JSON)
        );
    }

    private void createCommentOnNotRealPost_ReturnsNotFound() throws Exception {
        final UUID randomPostId = UUID.randomUUID();

        mockMvc.perform(
                MockMvcRequestBuilders.post(CommentURI.CREATE_COMMENT.formatted(randomPostId))
                        .header(USER_HEADER, DEFAULT_USER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonService.toJson(DEFAULT_COMMENT))
        ).andExpectAll(
                status().isNotFound()
        );
    }

    private void createNotValidComment_ReturnsBadRequest() throws Exception {
        final CommentRequest request = new CommentRequest("                      ");

        mockMvc.perform(
                MockMvcRequestBuilders.post(CommentURI.CREATE_COMMENT.formatted(DEFAULT_POST_ID))
                        .header(USER_HEADER, DEFAULT_USER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonService.toJson(request))
        ).andExpectAll(
                status().isBadRequest(),
                content().contentType(MediaType.APPLICATION_JSON)
        );
    }

    private void deleteComment_ReturnsOk() throws Exception {
        final UUID commentId = createComment(DEFAULT_USER, DEFAULT_COMMENT);

        mockMvc.perform(
                MockMvcRequestBuilders.delete(CommentURI.DELETE_COMMENT.formatted(DEFAULT_POST_ID, commentId))
                        .header(USER_HEADER, DEFAULT_USER)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isOk()
        );
    }

    private void deleteAnotherUserComment_ReturnsForbidden() throws Exception {
        final long authUserId = DEFAULT_USER + 1;
        final UUID commentId = createComment(DEFAULT_USER, DEFAULT_COMMENT);

        mockMvc.perform(
                MockMvcRequestBuilders.delete(CommentURI.DELETE_COMMENT.formatted(DEFAULT_POST_ID, commentId))
                        .header(USER_HEADER, authUserId)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isForbidden()
        );
    }

    private void deleteNotExistsComment_ReturnsNotFound() throws Exception {
        final UUID commentId = UUID.randomUUID();

        mockMvc.perform(
                MockMvcRequestBuilders.delete(CommentURI.DELETE_COMMENT.formatted(DEFAULT_POST_ID, commentId))
                        .header(USER_HEADER, DEFAULT_USER)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isNotFound()
        );
    }

    private UUID createComment(final long userId, final CommentRequest request) throws Exception {
        final String response = mockMvc.perform(
                MockMvcRequestBuilders.post(CommentURI.CREATE_COMMENT.formatted(DEFAULT_POST_ID))
                        .header(USER_HEADER, userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonService.toJson(request))
        ).andExpectAll(
                status().isCreated(),
                content().contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse().getContentAsString();

        return jsonService.extract(response, UUID.class);
    }

    private UUID createComment(final long userId, final String content) throws Exception {
        return createComment(userId, new CommentRequest(content));
    }
}
