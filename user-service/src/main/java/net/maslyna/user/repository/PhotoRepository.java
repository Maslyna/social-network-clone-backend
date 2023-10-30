package net.maslyna.user.repository;

import net.maslyna.user.model.entity.Photo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface PhotoRepository extends JpaRepository<Photo, UUID> {
    @Query("select p from Photo p where p.user.id = ?1")
    Page<Photo> findUserImages(Long id, Pageable pageable);
}