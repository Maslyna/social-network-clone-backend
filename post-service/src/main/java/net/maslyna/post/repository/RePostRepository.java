package net.maslyna.post.repository;

import net.maslyna.post.model.entity.post.RePost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RePostRepository extends JpaRepository<RePost, UUID> {
}