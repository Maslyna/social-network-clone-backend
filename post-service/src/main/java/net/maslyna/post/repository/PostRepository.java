package net.maslyna.post.repository;

import net.maslyna.post.model.PostStatus;
import net.maslyna.post.model.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PostRepository extends JpaRepository<Post, UUID> {
    Page<Post> findByUserIdAndStatus(Long userId, PostStatus status, Pageable pageable);
    Page<Post> findByUserId(Long userId, Pageable pageable);
    Page<Post> findByStatus(PostStatus status, Pageable pageable);
}