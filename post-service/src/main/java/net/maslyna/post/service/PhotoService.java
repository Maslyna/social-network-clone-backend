package net.maslyna.post.service;

import lombok.RequiredArgsConstructor;
import net.maslyna.common.model.FileType;
import net.maslyna.common.service.PropertiesMessageService;
import net.maslyna.post.client.FileClient;
import net.maslyna.post.exception.AccessDeniedException;
import net.maslyna.post.exception.NotFoundException;
import net.maslyna.post.model.entity.Photo;
import net.maslyna.post.repository.PhotoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class PhotoService {
    private final PropertiesMessageService messageService;
    private final PhotoRepository repository;
    private final FileClient fileClient;

    public Photo save(Long userId, FileType type, MultipartFile file) {
        final UUID photoId = UUID.randomUUID();
        String contentUrl = saveFile(userId, photoId, type, file);
        return createPhoto(userId, photoId, contentUrl);
    }

    public void delete(Long userId, UUID photoId) {
        Photo photo = getPhotoById(photoId);

        if (!photo.getUserId().equals(userId)) {
            throw new AccessDeniedException(HttpStatus.FORBIDDEN);
        }
        fileClient.delete(photoId, userId);
        repository.delete(photo);
    }

    public void delete(Long userId, Photo photo) {
        if (!photo.getUserId().equals(userId)) {
            throw new AccessDeniedException(HttpStatus.FORBIDDEN);
        }
        fileClient.delete(photo.getId(), userId);
        repository.delete(photo);
    }

    @Transactional(readOnly = true)
    public Photo getPhotoById(UUID photoId) {
        return repository.findById(photoId)
                .orElseThrow(() -> new NotFoundException(
                        HttpStatus.NOT_FOUND,
                        messageService.getProperty("error.photo.not-found", photoId)
                ));
    }

    private Photo createPhoto(Long userId, UUID photoId, String contentUrl) {
        return repository.save(
                Photo.builder()
                        .id(photoId)
                        .userId(userId)
                        .contentUrl(contentUrl)
                        .build()
        );
    }

    private String saveFile(Long userId, UUID photoId, FileType type, MultipartFile file) {
        ResponseEntity<String> response = fileClient.save(userId, photoId, type, file);

        return response.getBody();
    }
}
