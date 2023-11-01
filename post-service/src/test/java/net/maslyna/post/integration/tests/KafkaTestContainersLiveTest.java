package net.maslyna.post.integration.tests;

import net.maslyna.common.kafka.dto.CommentLikedEvent;
import net.maslyna.common.kafka.dto.PostCreatedEvent;
import net.maslyna.common.kafka.dto.PostLikedEvent;
import net.maslyna.post.model.CommentStatus;
import net.maslyna.post.model.PostStatus;
import net.maslyna.post.model.entity.Comment;
import net.maslyna.post.model.entity.post.Post;
import net.maslyna.post.producer.KafkaProducer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;


public class KafkaTestContainersLiveTest extends BasicIntegrationTest {

    @Autowired
    private KafkaProducer producer;

    private static final Post testPost = Post.builder()
            .id(UUID.randomUUID())
            .userId(1L)
            .status(PostStatus.PUBLISHED)
            .title("test title")
            .text("test text")
            .comments(Set.of())
            .likes(Set.of())
            .hashtags(Set.of())
            .createdAt(Instant.now())
            .build();

    private static final Comment testComment = Comment.builder()
            .id(UUID.randomUUID())
            .userId(1L)
            .status(CommentStatus.NORMAL)
            .text("test text")
            .comment(null)
            .post(testPost)
            .likes(Set.of())
            .comments(Set.of())
            .createdAt(Instant.now())
            .build();

    @Test
    public void sendPostCreatedEventTests() throws Exception {
        sendPostCreatedEvent_thenMessageReceived();
        sendNullPostCreatedEvent_thenMessageNotReceived();
    }

    @Test
    public void sendPostLikedEventTests() throws Exception {
        sendPostLikedEvent_thenMessageReceived();
        sendNullPostLikedEvent_thenMessageNotReceived();
    }

    @Test
    public void sendCommentLikedEventTests() throws Exception {
        sendCommentLikedEvent_thenMessageReceived();
        sendNullCommentLikedEvent_thenMessageNotReceived();
    }

    private void sendPostCreatedEvent_thenMessageReceived() throws Exception {
        producer.sendPostCreatedEvent(testPost);

        boolean messageConsumed = consumer.getLatch().await(10, TimeUnit.SECONDS);

        assertTrue(messageConsumed);
        assertEquals(consumer.getPayload().getClass(), PostCreatedEvent.class);

        PostCreatedEvent event = (PostCreatedEvent) consumer.getPayload();

        assertEquals(testPost.getId(), event.post());
        assertEquals(testPost.getUserId(), event.userId());
        assertEquals(testPost.getTitle(), event.title());
        consumer.reset();
    }

    private void sendNullPostCreatedEvent_thenMessageNotReceived() throws Exception {
        producer.sendPostCreatedEvent(null);
        boolean messageConsumed = consumer.getLatch().await(10, TimeUnit.SECONDS);
        assertFalse(messageConsumed);
        consumer.reset();
    }

    private void sendPostLikedEvent_thenMessageReceived() throws Exception {
        long authUserId = 2L;

        producer.sendPostLikedEvent(authUserId, testPost);

        boolean messageConsumed = consumer.getLatch().await(10, TimeUnit.SECONDS);

        assertTrue(messageConsumed);
        assertEquals(consumer.getPayload().getClass(), PostLikedEvent.class);

        PostLikedEvent event = (PostLikedEvent) consumer.getPayload();

        assertEquals(authUserId, event.userId());
        assertEquals(testPost.getUserId(), event.postOwnerId());
        assertEquals(testPost.getId(), event.postId());

        consumer.reset();
    }

    private void sendNullPostLikedEvent_thenMessageNotReceived() throws Exception {
        producer.sendPostLikedEvent(null, null);
        boolean messageConsumed = consumer.getLatch().await(10, TimeUnit.SECONDS);
        assertFalse(messageConsumed);
        consumer.reset();
    }

    private void sendCommentLikedEvent_thenMessageReceived() throws Exception {
        long authUserId = 2L;

        producer.sendCommentLikedEvent(authUserId, testComment);

        boolean messageConsumed = consumer.getLatch().await(10, TimeUnit.SECONDS);

        assertTrue(messageConsumed);
        assertEquals(consumer.getPayload().getClass(), CommentLikedEvent.class);

        CommentLikedEvent event = (CommentLikedEvent) consumer.getPayload();

        assertEquals(authUserId, event.userId());
        assertEquals(testComment.getUserId(), event.commentOwnerId());
        assertEquals(testComment.getId(), event.commentId());
        assertEquals(testComment.getPost().getId(), event.postId());

        consumer.reset();
    }

    private void sendNullCommentLikedEvent_thenMessageNotReceived() throws Exception {
        producer.sendCommentLikedEvent(null, null);
        boolean messageConsumed = consumer.getLatch().await(10, TimeUnit.SECONDS);
        assertFalse(messageConsumed);
        consumer.reset();
    }

}