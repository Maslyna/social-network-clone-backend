package net.maslyna.user.mapper.impl;

import net.maslyna.user.mapper.PhotoMapper;
import net.maslyna.user.model.dto.response.PhotoResponse;
import net.maslyna.user.model.entity.Photo;
import org.springframework.stereotype.Component;

@Component
public class PhotoMapperImpl implements PhotoMapper {
    @Override
    public PhotoResponse photoToPhotoResponse(Photo photo) {
        return PhotoResponse.builder()
                .photoId(photo.getId())
                .imageUrl(photo.getImageUrl()) //NOTE: may I should use file client?
                .build();
    }
}
