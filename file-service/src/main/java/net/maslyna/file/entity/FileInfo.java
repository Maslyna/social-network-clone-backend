package net.maslyna.file.entity;

import net.maslyna.common.model.FileType;

import java.util.UUID;

public interface FileInfo
        extends BaseEntity<UUID> {
    Long getUserId();
    void setUserId(Long userId);
    UUID getContentId();
    void setContentId(UUID contentId);
    String getFileName();
    void setFileName(String fileName);
    String getGcsFileLink();
    void setGcsFileLink(String gcsFileLink);
    FileType getFileType();
    void setFileType(FileType fileType);
}
