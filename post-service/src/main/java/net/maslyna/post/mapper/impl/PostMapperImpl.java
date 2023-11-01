package net.maslyna.post.mapper.impl;

import lombok.RequiredArgsConstructor;
import net.maslyna.common.kafka.dto.PostCreatedEvent;
import net.maslyna.post.mapper.PostMapper;
import net.maslyna.post.model.dto.response.HashtagResponse;
import net.maslyna.post.model.dto.response.PostResponse;
import net.maslyna.post.model.entity.Hashtag;
import net.maslyna.post.model.entity.post.Post;
import net.maslyna.post.model.entity.post.RePost;
import net.maslyna.post.repository.CommentRepository;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PostMapperImpl implements PostMapper {
    private final CommentRepository commentRepository;

    @Override
    public PostResponse postToPostResponse(Post post) {
        return PostResponse.builder()
                .postId(post.getId())
                .originalPost(getOriginalPostId(post))
                .userId(post.getUserId())
                .title(post.getTitle())
                .status(post.getStatus())
                .text(post.getText())
                .hashtags(setHashtagToSetHashtagResponse(post.getHashtags()))
                .commentsAmount(commentRepository.countCommensAmount(post.getId()))
                .likesAmount(countLikesAmount(post))
                .createdAt(post.getCreatedAt())
                .build();
    }

    @Override
    public PostCreatedEvent postToPostCreatedResponse(Post post) {
        return PostCreatedEvent.builder()
                .userId(post.getUserId())
                .title(post.getTitle())
                .post(post.getId())
                .rePost(getOriginalPostId(post))
                .createdAt(post.getCreatedAt())
                .build();
    }

    private Set<HashtagResponse> setHashtagToSetHashtagResponse(Set<Hashtag> hashtags) {
        return hashtags.stream().map(this::hashtagToHashtagResponse).collect(Collectors.toSet());
    }

    private UUID getOriginalPostId(Post post) {
        return  (post instanceof RePost) ? ((RePost) post).getOriginalPost().getId() : null;
    }

    private long countLikesAmount(Post post) {
        return post.getLikes() != null ? post.getLikes().stream().collect(Collectors.counting()) : 0;
    }

    private HashtagResponse hashtagToHashtagResponse(Hashtag hashtag) {
        return HashtagResponse.builder()
                .hashtagId(hashtag.getId())
                .text(hashtag.getText())
                .build();
    }
}
