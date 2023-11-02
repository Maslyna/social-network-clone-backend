package net.maslyna.post.repository;

import net.maslyna.post.model.entity.like.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface PostLikeRepository extends JpaRepository<PostLike, UUID> {
    @Query("select count(p) from PostLike p where p.post.id = ?1")
    long countLikesOnPost(UUID id);
    @Query("select (count(p) > 0) from PostLike p where p.userId = ?1 and p.post.id = ?2")
    boolean exitstByUserIdAndPostId(Long userId, UUID id);
    @Query("select p from PostLike p where p.userId = ?1 and p.post.id = ?2")
    Optional<PostLike> findLikeByUserIaAndPostId(Long userId, UUID id);
}