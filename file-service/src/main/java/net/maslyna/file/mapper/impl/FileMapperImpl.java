package net.maslyna.file.mapper.impl;

import lombok.RequiredArgsConstructor;
import net.maslyna.file.entity.FileEntity;
import net.maslyna.file.mapper.FileMapper;
import net.maslyna.file.response.FileResponse;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FileMapperImpl implements FileMapper {

    @Override
    public FileResponse fileToFileResponse(FileEntity entity, String link) {
        return FileResponse.builder()
                .fileId(entity.getId())
                .link(link)
                .userId(entity.getUserId())
                .build();
    }
}
