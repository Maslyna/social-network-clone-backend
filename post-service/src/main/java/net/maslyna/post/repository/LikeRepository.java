package net.maslyna.post.repository;

import net.maslyna.post.model.entity.like.AbstractLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface LikeRepository extends JpaRepository<AbstractLike, UUID> {
    default void deleteLike(AbstractLike like) {
        like.remove();
        this.delete(like);
    }
}