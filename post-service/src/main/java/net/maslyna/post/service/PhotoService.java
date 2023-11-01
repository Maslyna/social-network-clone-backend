package net.maslyna.post.service;

import lombok.RequiredArgsConstructor;
import net.maslyna.common.model.FileType;
import net.maslyna.post.client.FileClient;
import net.maslyna.post.model.entity.Photo;
import net.maslyna.post.repository.PhotoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PhotoService {
    private final PhotoRepository repository;
    private final FileClient fileClient;

    public Photo save(Long userId, FileType type, MultipartFile file) {
        final UUID photoId = UUID.randomUUID();
        String contentUrl = saveFile(userId, photoId, type, file);
        return createPhoto(userId, photoId, contentUrl);
    }

    private Photo createPhoto(Long userId, UUID photoId, String contentUrl) {
        return Photo.builder()
                .id(photoId)
                .userId(userId)
                .contentUrl(contentUrl)
                .build();
    }

    private String saveFile(Long userId, UUID contentId, FileType type, MultipartFile file) {
        ResponseEntity<String> response = fileClient.save(userId, contentId, type, file);

        return response.getBody();
    }
}
