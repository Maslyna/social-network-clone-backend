package net.maslyna.post.repository;

import net.maslyna.post.model.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

public interface LikeRepository extends JpaRepository<Like, UUID> {
    @Query("select l from Like l where l.userId = ?1 and l.post.id = ?2")
    Optional<Like> findByUserIdAndPostId(Long userId, UUID id);
    @Query("select l from Like l where l.userId = ?1 and l.comment.id = ?2")
    Optional<Like> findByUserIdAndCommentId(Long userId, UUID id);
    @Query("select (count(l) > 0) from Like l where l.userId = ?1 and l.comment.id = ?2")
    boolean existsByUserIdAndCommentId(Long userId, UUID commentId);
    @Transactional
    @Modifying
    @Query("delete from Like l where l.userId = ?1 and l.comment.id = ?2")
    int deleteByCommentAndUserId(Long userId, UUID commentId);
    @Transactional
    @Modifying
    @Query("delete from Like l where l.userId = ?1 and l.post.id = ?2")
    int deleteByPostAndUserId(Long userId, UUID postId);
    @Query("select (count(l) > 0) from Like l where l.userId = ?1 and l.post.id = ?2")
    boolean existsByUserIdAndPostId(Long userId, UUID id);
}