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
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private Long userId;

    private UUID contentId;

    private String fileName;

    private String gcsFileLink;

    @Enumerated(EnumType.STRING)
    private FileType fileType;
}
