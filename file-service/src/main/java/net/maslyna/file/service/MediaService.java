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

    String save(UUID contentId, Long userId, FileType type, MultipartFile file);

    @Transactional(readOnly = true)
    String getLink(UUID contentId);

    FileStatus remove(Long userId, UUID contentId);
}
