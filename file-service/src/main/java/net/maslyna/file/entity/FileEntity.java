package net.maslyna.file.entity;

import jakarta.persistence.*;
import lombok.*;
import net.maslyna.common.model.FileType;

import java.util.UUID;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Entity
public class FileEntity implements FileInfo {
    @Id
    private UUID id;

    private Long userId;

    private String fileName;

    @Enumerated(EnumType.STRING)
    private FileType fileType;

    private long size;
}
