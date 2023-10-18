package net.maslyna.post.repository;

import net.maslyna.post.model.PostStatus;
import net.maslyna.post.model.entity.post.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface PostRepository extends JpaRepository<Post, UUID> {
    @Query("""
            select p from Post p
            full outer join p.hashtags h
            where h.text in :hashtags
            and p.status = :status
            """)
    Page<Post> findPostsByStatusAndHashtags(
            @Param("hashtags") String[] hashtags,
            @Param("status") PostStatus status,
            Pageable pageable
    );
    Page<Post> findByUserIdAndStatus(Long userId, PostStatus status, Pageable pageable);
    Page<Post> findByUserId(Long userId, Pageable pageable);
    Page<Post> findByStatus(PostStatus status, Pageable pageable);
}