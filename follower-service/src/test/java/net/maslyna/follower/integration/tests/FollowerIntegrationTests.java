package net.maslyna.follower.integration.tests;

import net.maslyna.follower.integration.uri.ServiceURI;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class FollowerIntegrationTests extends BasicIntegrationTest {

    @Test
    public void followTests() throws Exception {
        follow_ReturnsOk();
        followSecondTime_ReturnsConflict();
        followMyself_ReturnsConflict();
    }

    @Test
    public void unfollowTests() throws Exception {
        unfollow_ReturnsOk();
        unfollowFromUserThatNotInSubscriptions_ReturnsBadRequest();
        unfollowFromMyself_ReturnsConflict();
    }

    @Test
    public void isFollowedTests() throws Exception {
        final long userId = DEFAULT_USER + 1;
        assertFalse(isFollowed(DEFAULT_USER, userId));
        follow(userId, DEFAULT_USER);
        assertTrue(isFollowed(DEFAULT_USER, userId));
        isFollowedUserOnHimself();
    }

    @Test
    public void isSubscribedTests() throws Exception {
        final long userId = DEFAULT_USER + 1;
        assertFalse(isSubscribed(DEFAULT_USER, userId));
        follow(DEFAULT_USER, userId);
        assertTrue(isSubscribed(DEFAULT_USER, userId));
        isSubscribedUserOnHimself();
    }

    @Test
    public void getSubscription_ReturnsOk() throws Exception {
        final long userId = DEFAULT_USER + 1;

        mockMvc.perform(
                MockMvcRequestBuilders.get(ServiceURI.GET_SUBSCRIPTIONS.formatted(userId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(USER_HEADER, DEFAULT_USER)
        ).andExpectAll(
                status().isOk()
        );
    }

    @Test
    public void getPrivateFollowers_ReturnsOk() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get(ServiceURI.GET_PRIVATE_FOLLOWERS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(USER_HEADER, DEFAULT_USER)
        ).andExpectAll(
                status().isOk()
        );
    }

    @Test
    public void getFollowers_ReturnsOk() throws Exception {
        final long userId = DEFAULT_USER + 1;

        mockMvc.perform(
                MockMvcRequestBuilders.get(ServiceURI.GET_FOLLOWERS.formatted(userId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(USER_HEADER, DEFAULT_USER)
        ).andExpectAll(
                status().isOk()
        );
    }

    @Test
    public void getPrivateSubscriptions_ReturnsOk() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get(ServiceURI.GET_PRIVATE_SUBSCRIPTIONS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(USER_HEADER, DEFAULT_USER)
        ).andExpectAll(
                status().isOk()
        );
    }

    @Test
    public void getSubscriptions_ReturnsOk() throws Exception {
        final long userId = DEFAULT_USER + 1;

        mockMvc.perform(
                MockMvcRequestBuilders.get(ServiceURI.GET_SUBSCRIPTIONS.formatted(userId))
                        .header(USER_HEADER, DEFAULT_USER)
        ).andExpectAll(
                status().isOk()
        );
    }

    private void follow_ReturnsOk() throws Exception {
        final long anotherUser = DEFAULT_USER + 1;

        follow(DEFAULT_USER, anotherUser);
        assertTrue(isSubscribed(DEFAULT_USER, anotherUser));
    }

    private void followSecondTime_ReturnsConflict() throws Exception {
        final long anotherUser = DEFAULT_USER + 1;

        mockMvc.perform(
                MockMvcRequestBuilders.post(ServiceURI.FOLLOW.formatted(anotherUser))
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

    private void unfollow_ReturnsOk() throws Exception {
        final long unfollowFrom = DEFAULT_USER + 1;
        follow(DEFAULT_USER, unfollowFrom);

        mockMvc.perform(
                MockMvcRequestBuilders.delete(ServiceURI.UNFOLLOW.formatted(unfollowFrom))
                        .header(USER_HEADER, DEFAULT_USER)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isOk()
        );
        assertFalse(isSubscribed(DEFAULT_USER, unfollowFrom));
    }

    private void unfollowFromUserThatNotInSubscriptions_ReturnsBadRequest() throws Exception {
        final long unfollowFrom = DEFAULT_USER + 1;

        mockMvc.perform(
                MockMvcRequestBuilders.delete(ServiceURI.UNFOLLOW.formatted(unfollowFrom))
                        .header(USER_HEADER, DEFAULT_USER)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isBadRequest()
        );
    }

    private void unfollowFromMyself_ReturnsConflict() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.delete(ServiceURI.UNFOLLOW.formatted(DEFAULT_USER))
                        .header(USER_HEADER, DEFAULT_USER)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isConflict()
        );
    }

    private void follow(final long userId, final long followTo) throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.post(ServiceURI.FOLLOW.formatted(followTo))
                        .header(USER_HEADER, userId)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isOk()
        );
    }

    private void isFollowedUserOnHimself() throws Exception {
        assertFalse(isFollowed(DEFAULT_USER, DEFAULT_USER));
    }

    private void isSubscribedUserOnHimself() throws Exception {
        assertFalse(isSubscribed(DEFAULT_USER, DEFAULT_USER));
    }

    private boolean isFollowed(final long userId, final long followedTo) throws Exception {
        String content = mockMvc.perform(
                MockMvcRequestBuilders.get(ServiceURI.IS_FOLLOWED.formatted(followedTo))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(USER_HEADER, userId)
        ).andExpectAll(
                status().isOk()
        ).andReturn().getResponse().getContentAsString();

        return jsonService.extract(content, Boolean.class);
    }

    private boolean isSubscribed(final long userId, final long subscribedTo) throws Exception {
        String content = mockMvc.perform(
                MockMvcRequestBuilders.get(ServiceURI.IS_SUBSCRIBED.formatted(subscribedTo))
                        .header(USER_HEADER, userId)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isOk()
        ).andReturn().getResponse().getContentAsString();

        return jsonService.extract(content, Boolean.class);
    }
}
