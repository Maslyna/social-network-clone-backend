package net.maslyna.post.mapper;

import net.maslyna.post.kafka.dto.PostCreatedResponse;
import net.maslyna.post.model.dto.response.HashtagResponse;
import net.maslyna.post.model.dto.response.PostResponse;
import net.maslyna.post.model.entity.Hashtag;
import net.maslyna.post.model.entity.post.AbstractPost;
import net.maslyna.post.model.entity.post.Post;
import net.maslyna.post.model.entity.post.RePost;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface PostMapper { //TODO: make own mappers will be better idea

    default PostResponse postToPostResponse(Post post) {
        return PostResponse.builder()
                .postId(post.getId())
                .originalPost(getOriginalPostId(post))
                .userId(post.getUserId())
                .title(post.getTitle())
                .status(post.getStatus())
                .text(post.getText())
                .hashtags(setHashtagToSetHashtagResponse(post.getHashtags()))
                .commentsAmount(countComments(post))
                .likesAmount(countLikes(post))
                .createdAt(post.getCreatedAt())
                .build();
    }

    default PostCreatedResponse postToPostCreatedResponse(Post post) {
        return PostCreatedResponse.builder()
                .userId(post.getUserId())
                .title(post.getTitle())
                .rePost(getOriginalPostId(post))
                .createdAt(post.getCreatedAt())
                .build();
    }

    default UUID getOriginalPostId(Post post) {
        return  (post instanceof RePost) ? ((RePost) post).getOriginalPost().getId() : null;
    }


    default Set<HashtagResponse> setHashtagToSetHashtagResponse(Set<Hashtag> hashtags) {
        return hashtags.stream().map(this::hashtagToHashtagResponse).collect(Collectors.toSet());
    }

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
