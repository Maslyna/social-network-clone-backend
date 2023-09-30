package net.maslyna.post.repository;

import net.maslyna.post.model.PostStatus;
import net.maslyna.post.model.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface PostRepository extends JpaRepository<Post, UUID> {
    @Query("""
            select p from Post p inner join p.hashtags hashtags
            where hashtags.text in lower(?1)
            and p.status = ?2
            """)
    Page<Post> findPostsByStatusAndHashtags(String[] hashtags, PostStatus status, Pageable pageable);
    Page<Post> findByUserIdAndStatus(Long userId, PostStatus status, Pageable pageable);
    Page<Post> findByUserId(Long userId, Pageable pageable);
    Page<Post> findByStatus(PostStatus status, Pageable pageable);
}