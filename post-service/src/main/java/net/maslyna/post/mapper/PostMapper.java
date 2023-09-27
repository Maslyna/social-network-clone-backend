package net.maslyna.post.mapper;

import net.maslyna.post.model.dto.response.FullPostResponse;
import net.maslyna.post.model.dto.response.HashtagResponse;
import net.maslyna.post.model.dto.response.PostResponse;
import net.maslyna.post.model.entity.Hashtag;
import net.maslyna.post.model.entity.Post;
import net.maslyna.post.repository.PostRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface PostMapper {

    @Mapping(target = "postId", expression = "java(post.getId())")
    @Mapping(target = "hashtags",
            expression = "java(post.getHashtags().stream().map(this::hashtagToHashtagResponse).collect(java.util.stream.Collectors.toSet()))")
    @Mapping(target = "text", expression = "java(post.getText())")
    @Mapping(target = "likesAmount", expression = "java(this.countLikes(post))")
    @Mapping(target = "commentsAmount", expression = "java(this.countComments(post))")
    PostResponse postToPostResponse(Post post);

    @Mapping(target = "hashtagId", expression = "java(hashtag.getId())")
    @Mapping(target = "text", expression = "java(hashtag.getText())")
    HashtagResponse hashtagToHashtagResponse(Hashtag hashtag);


    //TODO: Not so fast as can be. Need to learn about data caching.
    default long countComments(Post post) {
        return post.getComments().stream().collect(Collectors.counting());
    }

    default long countLikes(Post post) {
        return post.getLikes().stream().collect(Collectors.counting());
    }
}
