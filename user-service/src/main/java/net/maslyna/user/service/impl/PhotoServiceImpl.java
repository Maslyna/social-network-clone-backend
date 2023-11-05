package net.maslyna.user.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.maslyna.common.model.FileType;
import net.maslyna.common.service.PropertiesMessageService;
import net.maslyna.user.client.FileClient;
import net.maslyna.user.client.response.FileStatus;
import net.maslyna.user.exception.GlobalUserServiceException;
import net.maslyna.user.exception.NotPublicAccountException;
import net.maslyna.user.model.entity.Photo;
import net.maslyna.user.model.entity.User;
import net.maslyna.user.repository.PhotoRepository;
import net.maslyna.user.service.PhotoService;
import net.maslyna.user.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PhotoServiceImpl implements PhotoService {
    private final PropertiesMessageService messageService;
    private final UserService userService;
    private final PhotoRepository photoRepository;
    private final FileClient fileClient;

    @Override
    public void setProfilePhoto(Long userId, MultipartFile file) {
        final User user = userService.getUser(userId);
        final UUID photoId = UUID.randomUUID();

        Photo photo = createPhoto(user, photoId, file);

        user.setProfilePhoto(photo);
        user.getUserPhotos().add(photo);
    }

    @Override
    public UUID addPhoto(Long userId, MultipartFile file) {
        final User user = userService.getUser(userId);
        final UUID photoId = UUID.randomUUID();

        Photo photo = createPhoto(user, photoId, file);
        user.addPhoto(photo);
        return photo.getId();
    }

    @Override
    public ResponseEntity<String> getLink(UUID photoId) {
        return fileClient.getLink(photoId);
    }

    @Override
    public ResponseEntity<FileStatus> removePhoto(Long userId, UUID photoId) {
        final User user = userService.getUser(userId);
        final Photo photo = getPhotoById(photoId);

        if (!user.getId().equals(photo.getUser().getId())) {
            throw new GlobalUserServiceException(HttpStatus.FORBIDDEN); //TODO: create custom exception
        }
        user.removePhoto(photo);
        photoRepository.delete(photo);
        return fileClient.delete(photo.getId(), user.getId());
    }

    @Override
    public Page<Photo> getUserPhoto(Long userId, Long authUserId, Pageable pageable) {
        if (!authUserId.equals(userId)) {
            final User user = userService.getUser(userId);
            if (!user.isPublicAccount()) {
                throw new NotPublicAccountException(
                        HttpStatus.FORBIDDEN,
                        messageService.getProperty("error.user.not-public-account")
                );
            }
        }
        return photoRepository.findUserImages(userId, pageable);
    }

    private Photo getPhotoById(UUID photoId) {
        return photoRepository.findById(photoId)
                .orElseThrow(() ->
                        new GlobalUserServiceException(HttpStatus.NOT_FOUND, "photo not found")
                ); //TODO: refactor
    }

    private String saveUserPhoto(Long userId, MultipartFile file, UUID photoId) {
        ResponseEntity<String> response = fileClient.save(userId, photoId, FileType.USER_IMG, file);

        if (!response.getStatusCode().is2xxSuccessful()
                || response.getBody() == null
                || response.getBody().isBlank()) {
            throw new GlobalUserServiceException(HttpStatus.NOT_IMPLEMENTED, "file not saved");
        }

        return response.getBody();
    }

    private Photo createPhoto(User user, UUID photoId, MultipartFile file) {
        final String imageUrl = saveUserPhoto(user.getId(), file, photoId);

        return photoRepository.save(
                Photo.builder()
                        .id(photoId)
                        .imageUrl(imageUrl)
                        .user(user)
                        .createdAt(Instant.now())
                        .build()
        );
    }
}
