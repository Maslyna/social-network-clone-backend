package net.maslyna.post.repository;

import net.maslyna.post.model.entity.like.CommentLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface CommentLikeRepository extends JpaRepository<CommentLike, UUID> {
    @Query("select count(c) from CommentLike c where c.comment.id = ?1")
    long countLikesOnComment(UUID commentId);
    @Query("select (count(c) > 0) from CommentLike c where c.userId = ?1 and c.comment.id = ?2")
    boolean existsByUserIdAndCommentId(Long userId, UUID id);
    @Query("select c from CommentLike c where c.userId = ?1 and c.comment.id = ?2")
    Optional<CommentLike> findLikeByUserIdAndCommentId(Long userId, UUID id);
}