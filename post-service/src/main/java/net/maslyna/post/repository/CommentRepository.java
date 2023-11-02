package net.maslyna.post.repository;

import net.maslyna.post.model.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

public interface CommentRepository extends JpaRepository<Comment, UUID> {
    @Transactional(readOnly = true)
    @Query("select count(c) from Comment c where c.post.id = ?1")
    long countCommentsAmount(UUID postId);
}