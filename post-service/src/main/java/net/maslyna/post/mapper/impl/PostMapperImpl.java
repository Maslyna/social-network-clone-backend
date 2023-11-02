package net.maslyna.post.mapper.impl;

import lombok.RequiredArgsConstructor;
import net.maslyna.common.kafka.dto.PostCreatedEvent;
import net.maslyna.post.mapper.PostMapper;
import net.maslyna.post.model.dto.response.HashtagResponse;
import net.maslyna.post.model.dto.response.PhotoResponse;
import net.maslyna.post.model.dto.response.PostResponse;
import net.maslyna.post.model.entity.Hashtag;
import net.maslyna.post.model.entity.Photo;
import net.maslyna.post.model.entity.post.Post;
import net.maslyna.post.model.entity.post.RePost;
import net.maslyna.post.repository.CommentRepository;
import net.maslyna.post.repository.PostLikeRepository;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PostMapperImpl implements PostMapper {
    private final CommentRepository commentRepository;
    private final PostLikeRepository likeRepository;

    @Override
    public PostResponse postToPostResponse(Post post) {
        if (post == null) {
            return null;
        }

        return PostResponse.builder()
                .postId(post.getId())
                .originalPost(getOriginalPostId(post))
                .userId(post.getUserId())
                .title(post.getTitle())
                .status(post.getStatus())
                .text(post.getText())
                .hashtags(setHashtagToSetHashtagResponse(post.getHashtags()))
                .photos(setPhotosToSetPhotoResponse(post.getPhotos()))
                .commentsAmount(commentRepository.countCommentsAmount(post.getId()))
                .likesAmount(likeRepository.countLikesOnPost(post.getId()))
                .createdAt(post.getCreatedAt())
                .build();
    }

    @Override
    public PostCreatedEvent postToPostCreatedResponse(Post post) {
        if (post == null) {
            return null;
        }

        return PostCreatedEvent.builder()
                .userId(post.getUserId())
                .title(post.getTitle())
                .post(post.getId())
                .rePost(getOriginalPostId(post))
                .createdAt(post.getCreatedAt())
                .build();
    }

    private Set<PhotoResponse> setPhotosToSetPhotoResponse(Set<Photo> photos) {
        return photos != null
                ? photos.stream().map(this::photoToPhotoResponse).collect(Collectors.toSet())
                : null;
    }

    private Set<HashtagResponse> setHashtagToSetHashtagResponse(Set<Hashtag> hashtags) {
        return hashtags != null
                ? hashtags.stream().map(this::hashtagToHashtagResponse).collect(Collectors.toSet())
                : null;
    }

    private UUID getOriginalPostId(Post post) {
        return  (post instanceof RePost) ? ((RePost) post).getOriginalPost().getId() : null;
    }

    private HashtagResponse hashtagToHashtagResponse(Hashtag hashtag) {
        return HashtagResponse.builder()
                .hashtagId(hashtag.getId())
                .text(hashtag.getText())
                .build();
    }

    private PhotoResponse photoToPhotoResponse(Photo photo) {
        return PhotoResponse.builder()
                .photoId(photo.getId())
                .url(photo.getContentUrl())
                .build();
    }
}
