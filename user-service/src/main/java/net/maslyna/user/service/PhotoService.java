package net.maslyna.user.service;

import net.maslyna.user.client.FileStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Transactional
public interface PhotoService {
    void setProfilePhoto(Long userId, MultipartFile file);

    void addPhoto(Long userId, MultipartFile file);

    ResponseEntity<String> getLink(UUID photoId);

    ResponseEntity<FileStatus> removePhoto(Long userId, UUID photoId);
}
