package net.maslyna.user.mapper;

import net.maslyna.user.model.dto.response.PhotoResponse;
import net.maslyna.user.model.entity.Photo;

public interface PhotoMapper {
    PhotoResponse photoToPhotoResponse(Photo photo);
}
