package net.maslyna.file.entity;

import net.maslyna.common.model.FileType;

import java.util.UUID;

public interface FileInfo
        extends BaseEntity<UUID> {
    Long getUserId();
    String getFileName();
    FileType getFileType();
    long getSize();
}
