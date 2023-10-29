package net.maslyna.file.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.maslyna.common.model.FileType;
import net.maslyna.common.service.PropertiesMessageService;
import net.maslyna.file.entity.FileEntity;
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
    public String save(
            MultipartFile file,
            Long userId,
            UUID contentId,
            FileType type
    ) {
        isFileValid(file);
        if (userId == null || contentId == null || type == null) {
            throw new GlobalFileServiceException(HttpStatus.BAD_REQUEST); //TODO: make custom exceptions with message
        }

        FileEntity entity = createFile(file, userId, contentId, type);
        try {
            return storageService.upload(entity.getFileName(), file.getInputStream(), file.getContentType())
                    .getMediaLink();
        } catch (IOException e) {
            throw new GlobalFileServiceException(
                    HttpStatus.BAD_REQUEST,
                    messageService.getProperty("error.file.not-valid")
            );
        }
    }

    @Override
    public List<FileEntity> getFiles(
            UUID contentId,
            FileType type
    ) {
        if (contentId == null) {
            throw new GlobalFileServiceException(HttpStatus.BAD_REQUEST); //TODO: make custom exceptions with message
        }

        return type == null
                ? fileRepository.findByContentId(contentId)
                : fileRepository.findByContentIdAndType(contentId, type);
    }

    @Override
    public List<FileEntity> getUserFiles(
            Long userId
    ) {
        if (userId == null) {
            throw new GlobalFileServiceException(HttpStatus.BAD_REQUEST); //TODO: make custom exceptions with message
        }

        return fileRepository.findFilesByUserId(userId);
    }

    @Override
    @Transactional
    public FileStatus remove(
            List<UUID> filesIds
    ) {
        if (filesIds == null || filesIds.isEmpty()) {
            throw new GlobalFileServiceException(HttpStatus.BAD_REQUEST); //TODO: make custom exceptions with message
        }

        List<FileEntity> files = fileRepository.findAllById(filesIds);

        files.stream().filter(file -> storageService.delete(file.getGcsFileLink()))
                .forEach(file -> {
                    fileRepository.delete(file);
                    files.remove(file);
                });

        if (!files.isEmpty()) {
            List<UUID> undeletedFiles = files.stream()
                    .map(FileEntity::getId)
                    .toList();
            return FileStatus.builder()
                    .status("not all files were deleted")
                    .details(Map.of(
                            "error",
                            messageService.getProperty("error.file.multiple-delete-exception", undeletedFiles)
                    )).build();
        }
        return FileStatus.builder()
                .status("files deleted  successfully")
                .build();
    }

    @Override
    public void remove(
            UUID fileId
    ) {
        FileEntity entity = getFileById(fileId);
        if (!storageService.delete(entity.getGcsFileLink())) {
            throw new GlobalFileServiceException(
                    HttpStatus.NOT_IMPLEMENTED,
                    messageService.getProperty("error.file.delete-exception") //TODO: make custom exceptions with message
            );
        }
        fileRepository.delete(entity);
    }

    @Override
    public String getLink(
            UUID fileId
    ) {
        FileEntity entity = getFileById(fileId);
        return storageService.getLink(entity.getGcsFileLink());
    }

    @Override
    public String getLink(
            FileEntity entity
    ) {
        return storageService.getLink(entity.getGcsFileLink());
    }


    private FileEntity getFileById(UUID fileId) {
        if (fileId == null) {
            throw new GlobalFileServiceException(HttpStatus.BAD_REQUEST); //TODO: make custom exceptions with message
        }
        return fileRepository.findById(fileId)
                .orElseThrow(() -> new GlobalFileServiceException(HttpStatus.NOT_FOUND, "error.file.not-found"));
    }

    private FileEntity createFile(MultipartFile file, Long userId, UUID contentId, FileType type) {
        final String filename = "%s/%s".formatted(type, contentId + file.getOriginalFilename());
        return fileRepository.save(
                FileEntity.builder()
                        .fileType(type)
                        .contentId(contentId)
                        .userId(userId)
                        .fileName(filename)
                        .gcsFileLink(filename)
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
