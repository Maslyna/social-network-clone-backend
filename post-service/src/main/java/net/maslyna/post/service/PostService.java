package net.maslyna.post.service;

import net.maslyna.post.model.dto.request.PostRequest;
import net.maslyna.post.model.entity.post.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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

    String uploadPhoto(Long authenticatedUserId, UUID postId, MultipartFile file);

    void deletePhoto(Long authenticatedUserId, UUID postId, UUID photoId);

    @Transactional(readOnly = true)
    boolean isPostExists(UUID postId);

    @Transactional(readOnly = true)
    void checkIsPostExists(UUID postId);
}
