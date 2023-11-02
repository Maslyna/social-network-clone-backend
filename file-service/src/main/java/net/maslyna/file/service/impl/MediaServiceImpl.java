package net.maslyna.file.service.impl;

import com.google.cloud.storage.Blob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.maslyna.common.model.FileType;
import net.maslyna.common.service.PropertiesMessageService;
import net.maslyna.file.entity.FileEntity;
import net.maslyna.file.entity.FileInfo;
import net.maslyna.file.exception.GlobalFileServiceException;
import net.maslyna.file.repository.FileRepository;
import net.maslyna.file.response.FileStatus;
import net.maslyna.file.service.MediaService;
import net.maslyna.file.service.StorageService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;


@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class MediaServiceImpl implements MediaService {
    private final FileRepository fileRepository;
    private final StorageService storageService;
    private final PropertiesMessageService messageService;
    private final List<String> VALID_CONTENT_TYPES; //DON'T CHANGE NAME

    @Override
    public String save(UUID contentId, Long userId, FileType type, MultipartFile file) {
        isFileValid(file);
        FileInfo fileInfo = createFile(contentId, userId, type, file);
        try {
            Blob blob = storageService.upload(fileInfo.getFileName(), file.getInputStream(), file.getContentType());
            return blob.getMediaLink();
        } catch (IOException e) {
            throw new GlobalFileServiceException(HttpStatus.BAD_REQUEST, "error.file.not-valid");
        }
    }

    @Override
    public String getLink(UUID contentId) {
        FileInfo fileInfo = getFileById(contentId);
        return storageService.getLink(fileInfo.getFileName());
    }

    @Override
    public FileStatus remove(Long userId, UUID contentId) {
        FileEntity entity = getFileById(contentId);
        if (!entity.getUserId().equals(userId)) {
            throw new GlobalFileServiceException(HttpStatus.FORBIDDEN);
        }
        boolean removed = storageService.delete(entity.getFileName());
        String status = removed ? "file was removed" : "file wasn't removed";
        return FileStatus.builder()
                .status(status)
                .build();
    }

    private FileEntity getFileById(UUID fileId) {
        return fileRepository.findById(fileId)
                .orElseThrow(() -> new GlobalFileServiceException(HttpStatus.NOT_FOUND, "error.file.not-found"));
    }

    private FileEntity createFile(UUID contentId, Long userId, FileType type, MultipartFile file) {
        final String filename = "%s/%s/%s_%s".formatted(type, userId, contentId, Instant.now());
        return fileRepository.save(
                FileEntity.builder()
                        .id(contentId)
                        .fileType(type)
                        .userId(userId)
                        .fileName(filename)
                        .size(file.getSize())
                        .build()
        );
    }


    private void isFileValid(MultipartFile file) {
        final List<String> errorDetails = new ArrayList<>();
        boolean isValid = true;

        if (file != null) {
            if (file.getOriginalFilename() == null || file.getOriginalFilename().isBlank()) {
                isValid = false;
                errorDetails.add(messageService.getProperty("error.file.not-valid-name"));
            }
            if (!VALID_CONTENT_TYPES.contains(file.getContentType())) {
                isValid = false;
                errorDetails.add(messageService.getProperty("error.file.not-valid-content-type", file.getContentType()));
            }
            if (file.getSize() <= 0) {
                isValid = false;
                errorDetails.add(messageService.getProperty("error.file.is-empty"));
            }
        } else {
            isValid = false;
            errorDetails.add(messageService.getProperty("error.file.is-null"));
        }
        if (!isValid) {
            throw new GlobalFileServiceException(
                    HttpStatus.BAD_REQUEST,
                    messageService.getProperty("error.file.not-valid"),
                    Map.of("errors", errorDetails)
            );
        }
    }
}
