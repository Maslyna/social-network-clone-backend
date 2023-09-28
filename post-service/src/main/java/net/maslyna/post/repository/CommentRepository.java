package net.maslyna.post.repository;

import net.maslyna.post.model.entity.Comment;
import net.maslyna.post.model.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CommentRepository extends JpaRepository<Comment, UUID> {
    Page<Comment> findByPost(Post post, Pageable pageable);
}