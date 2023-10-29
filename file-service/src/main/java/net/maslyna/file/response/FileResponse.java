package net.maslyna.file.response;

import lombok.Builder;
import net.maslyna.common.model.FileType;

import java.util.UUID;

@Builder
public record FileResponse(
        Long userId,
        UUID fileId,
        UUID contentId,
        FileType type,
        String link
) {
}
