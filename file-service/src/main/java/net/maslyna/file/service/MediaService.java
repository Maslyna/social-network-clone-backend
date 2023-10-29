package net.maslyna.file.service;

import jakarta.annotation.Nullable;
import net.maslyna.common.model.FileType;
import net.maslyna.file.entity.FileEntity;
import net.maslyna.file.response.FileStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Transactional
public interface MediaService {
    String save(
            MultipartFile file,
            Long userId,
            UUID contentId,
            FileType type
    );

    List<FileEntity> getFiles(
            UUID contentId,
            @Nullable FileType type
    );

    List<FileEntity> getUserFiles(
            Long userId
    );

    FileStatus remove(
            List<UUID> filesIds
    );

    void remove(
            UUID fileId
    );

    String getLink(
            UUID fileId
    );


    String getLink(
            FileEntity entity
    );
}
