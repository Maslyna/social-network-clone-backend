package net.maslyna.file.repository;

import net.maslyna.file.entity.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface FileRepository extends JpaRepository<FileEntity, UUID> {
    @Query("select f from FileEntity f where f.contentId = ?1")
    List<FileEntity> findByContentId(UUID contentId);
}