package net.maslyna.user.service;

import net.maslyna.user.client.response.FileStatus;
import net.maslyna.user.model.entity.Photo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Transactional
public interface PhotoService {
    void setProfilePhoto(Long userId, MultipartFile file);

    UUID addPhoto(Long userId, MultipartFile file);

    ResponseEntity<String> getLink(UUID photoId);

    ResponseEntity<FileStatus> removePhoto(Long userId, UUID photoId);

    Page<Photo> getUserPhoto(Long userId, Long authUserId, Pageable pageable);
}
