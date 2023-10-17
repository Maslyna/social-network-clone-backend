package net.maslyna.post.service;

import net.maslyna.post.model.dto.request.PostRequest;
import net.maslyna.post.model.entity.post.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

public interface PostService {
    @Transactional(readOnly = true)
    Page<Post> getPosts(String[] hashtags, PageRequest pageRequest);

    @Transactional(readOnly = true)
    Page<Post> getPersonPosts(Long userId, Long authenticatedUserId, PageRequest pageRequest);

    @Transactional(readOnly = true)
    Post getPost(Long authenticatedUserId, UUID postId);

    UUID createPost(Long userId, PostRequest request);

    UUID createRepost(Long userId, UUID postId, PostRequest request);

    UUID editPost(
            Long authenticatedUserId,
            UUID postId,
            PostRequest request
    );

    void deletePost(Long authenticatedUserId, UUID postId);
}
