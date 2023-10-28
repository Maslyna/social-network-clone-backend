package net.maslyna.file.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.maslyna.common.model.FileType;
import net.maslyna.common.service.PropertiesMessageService;
import net.maslyna.file.entity.FileEntity;
import net.maslyna.file.exception.GlobalFileServiceException;
import net.maslyna.file.repository.FileRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;


@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class MediaService {
    private final FileRepository fileRepository;
    private final StorageService storageService;
    private final PropertiesMessageService messageService;
    private final List<String> VALID_CONTENT_TYPES; //DON'T CHANGE NAME


    public String save(
            MultipartFile file,
            Long userId,
            UUID contentId,
            FileType type
    ) {
        isFileValid(file);
        FileEntity entity = createFile(file, userId, contentId, type);
        try {
            return storageService.upload(entity.getFileName(), file.getInputStream(), file.getContentType())
                    .getMediaLink();
        } catch (IOException ignored) {
            return null;
        }
    }

    public List<FileEntity> getFiles(
            UUID contentId
    ) {
        return fileRepository.findByContentId(contentId);
    }

    public Map<String, Object> remove(
            List<UUID> filesIds
    ) {
        List<FileEntity> files = fileRepository.findAllById(filesIds);

        for (FileEntity file : files) {
            if (storageService.delete(file.getGcsFileLink())) {
                fileRepository.delete(file);
                files.remove(file);
            }
        }
        if (!files.isEmpty()) {
            List<UUID> undeletedFiles = files.stream()
                    .map(FileEntity::getId)
                    .toList();
            return Map.of("status", messageService
                    .getProperty("error.file.multiple-delete-exception", undeletedFiles));
        }
        return Map.of("status", "files deleted successfully");
    }

    public void remove(
            UUID fileId
    ) {
        FileEntity entity = getFileById(fileId);
        if (!storageService.delete(entity.getGcsFileLink())) {
            throw new GlobalFileServiceException(
                    HttpStatus.NOT_IMPLEMENTED,
                    messageService.getProperty("error.file.delete-exception")
            );
        }
        fileRepository.delete(entity);
    }

    public String getLink(
            UUID fileId
    ) {
        FileEntity entity = getFileById(fileId);
        return storageService.getLink(entity.getGcsFileLink());
    }

    public String getLink(
            FileEntity entity
    ) {
        return storageService.getLink(entity.getGcsFileLink());
    }

    private FileEntity getFileById(UUID fileId) {
        return fileRepository.findById(fileId)
                .orElseThrow(() -> new GlobalFileServiceException(HttpStatus.NOT_FOUND, "error.file.not-found"));
    }

    private FileEntity createFile(MultipartFile file, Long userId, UUID contentId, FileType type) {
        final String filename = file.getOriginalFilename() + contentId;
        return fileRepository.save(
                FileEntity.builder()
                        .fileType(type)
                        .contentId(contentId)
                        .userId(userId)
                        .fileName(filename)
                        .gcsFileLink(createGcsFileLink(type, filename))
                        .build()
        );
    }

    private String createGcsFileLink(FileType type, String filename) {
        return "%s/%s".formatted(type, filename);
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
