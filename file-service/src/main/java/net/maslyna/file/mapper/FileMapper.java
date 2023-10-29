package net.maslyna.file.mapper;

import net.maslyna.file.entity.FileEntity;
import net.maslyna.file.response.FileResponse;

public interface FileMapper {

    FileResponse fileToFileResponse(FileEntity entity, String link);
}
