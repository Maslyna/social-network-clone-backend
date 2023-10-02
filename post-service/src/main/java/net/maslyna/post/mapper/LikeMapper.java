package net.maslyna.post.mapper;

import net.maslyna.post.model.dto.response.LikeResponse;
import net.maslyna.post.model.entity.like.AbstractLike;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface LikeMapper {

    @Mapping(target = "likeId", expression = "java(abstractLike.getId())")
    LikeResponse likeToLikeResponse(AbstractLike abstractLike);
}
