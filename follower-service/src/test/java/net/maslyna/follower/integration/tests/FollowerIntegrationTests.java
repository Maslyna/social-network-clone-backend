package net.maslyna.follower.integration.tests;

import net.maslyna.follower.integration.uri.ServiceURI;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class FollowerIntegrationTests extends BasicIntegrationTest {

    @Test
    public void followTests() throws Exception {
        follow_ReturnsOk();
        followSecondTime_ReturnsConflict();
        followMyself_ReturnsConflict();
    }

    private void follow_ReturnsOk() throws Exception {
        final long userId = DEFAULT_USER + 1;

        mockMvc.perform(
                MockMvcRequestBuilders.post(ServiceURI.FOLLOW.formatted(userId))
                        .header(USER_HEADER, DEFAULT_USER)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isOk()
        );
    }

    private void followSecondTime_ReturnsConflict() throws Exception {
        final long userId = DEFAULT_USER + 1;

        mockMvc.perform(
                MockMvcRequestBuilders.post(ServiceURI.FOLLOW.formatted(userId))
                        .header(USER_HEADER, DEFAULT_USER)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isConflict()
        );
    }

    private void followMyself_ReturnsConflict() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.post(ServiceURI.FOLLOW.formatted(DEFAULT_USER))
                        .header(USER_HEADER, DEFAULT_USER)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isConflict()
        );
    }
}
