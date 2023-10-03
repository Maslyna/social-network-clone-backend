package net.maslyna.post.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.maslyna.post.exception.AccessDeniedException;
import net.maslyna.post.exception.PostNotFoundException;
import net.maslyna.post.model.PostStatus;
import net.maslyna.post.model.dto.request.PostRequest;
import net.maslyna.post.model.entity.Hashtag;
import net.maslyna.post.model.entity.post.Post;
import net.maslyna.post.model.entity.post.RePost;
import net.maslyna.post.repository.HashtagRepository;
import net.maslyna.post.repository.PostRepository;
import net.maslyna.post.repository.RePostRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PostService {
    private final PostRepository postRepository;
    private final RePostRepository rePostRepository;
    private final HashtagRepository hashtagRepository;
    private final PropertiesMessageService messageService;

    @Transactional(readOnly = true)
    public Page<Post> getPosts(String[] hashtags, PageRequest pageRequest) {
        return hashtags != null
                ? postRepository.findPostsByStatusAndHashtags(hashtags, PostStatus.PUBLISHED, pageRequest)
                : postRepository.findByStatus(PostStatus.PUBLISHED, pageRequest);
    }

    @Transactional(readOnly = true)
    public Page<Post> getPersonPosts(Long userId, Long authenticatedUserId, PageRequest pageRequest) {
        return !userId.equals(authenticatedUserId)
                ? getPublicPosts(userId, pageRequest)
                : getPrivatePosts(userId, pageRequest);
    }

    @Transactional(readOnly = true)
    public Post getPost(Long authenticatedUserId, UUID postId) {
        Post post = getPostById(postId);

        if (!post.getStatus().equals(PostStatus.PUBLISHED)
                && !post.getUserId().equals(authenticatedUserId)) {
            throw new AccessDeniedException(
                    FORBIDDEN,
                    messageService.getProperty("error.access.denied")
            );
        }
        return post;
    }

    public UUID createPost(Long userId, PostRequest request) {
        Post post = createNewPost(userId, request);
        log.info("post with id = {} created", post.getId());
        return post.getId();
    }

    public UUID createRepost(Long userId, UUID postId, PostRequest request) {
        Post post = getPost(userId, postId);
        RePost rePost = createRePost(userId, request, post);
        log.info("repost with id = {} created", rePost.getId());
        return rePost.getId();
    }

    public UUID editPost(
            Long authenticatedUserId,
            UUID postId,
            PostRequest request) {
        Post post = getPostById(postId);

        if (!post.getUserId().equals(authenticatedUserId)) {
            throw new AccessDeniedException(
                    FORBIDDEN,
                    messageService.getProperty("error.access.denied")
            );
        }

        if (request.text() != null) {
            post.setText(request.text());
        }
        if (request.title() != null) {
            post.setTitle(request.title());
        }
        if (!request.hashtags().isEmpty()) {
            post.setHashtags(createHashtagSet(request.hashtags()));
        }
        post.setStatus(request.status());

        return post.getId();
    }

    public void deletePost(Long authenticatedUserId, UUID postId) {
        Post post = getPostById(postId);

        if (!post.getUserId().equals(authenticatedUserId)) {
            throw new AccessDeniedException(
                    FORBIDDEN,
                    messageService.getProperty("error.access.denied")
            );
        }

        postRepository.delete(post);
        log.info("post with id = {} was deleted", post.getId());
    }

    private Page<Post> getPublicPosts(Long userId, PageRequest pageRequest) {
        return postRepository.findByUserIdAndStatus(userId, PostStatus.PUBLISHED, pageRequest);
    }

    private Page<Post> getPrivatePosts(Long userId, PageRequest pageRequest) {
        return postRepository.findByUserId(userId, pageRequest);
    }

    private Post createNewPost(Long userId, PostRequest request) {
        return postRepository.save(
                Post.builder() // TODO: after creating file service add opportunity to upload images
                        .userId(userId)
                        .status(request.status())
                        .createdAt(Instant.now())
                        .title(request.title())
                        .text(request.text())
                        .hashtags(createHashtagSet(request.hashtags()))
                        .build()
        );
    }

    private RePost createRePost(Long userId, PostRequest request, Post post) {
        return rePostRepository.save(
                RePost.builder() // TODO: after creating file service add opportunity to upload images
                        .userId(userId)
                        .status(request.status())
                        .createdAt(Instant.now())
                        .title(request.title())
                        .text(request.text())
                        .hashtags(createHashtagSet(request.hashtags()))
                        .originalPost(post)
                        .build()
        );
    }

    //TODO: separate logic in new class
    private Set<Hashtag> createHashtagSet(Set<String> hashtags) {
        return hashtags.stream()
                .map(this::saveOrGetHashtag)
                .collect(Collectors.toSet());
    }

    private Hashtag saveOrGetHashtag(String text) {
        return hashtagRepository.findByText(text)
                .orElseGet(() -> hashtagRepository.save(
                        Hashtag.builder()
                                .text(text)
                                .build()
                ));
    }

    private Post getPostById(UUID postId) {
        return postRepository.findById(postId)
                .orElseThrow(() ->
                        new PostNotFoundException(
                                NOT_FOUND,
                                messageService.getProperty("error.post.not-found", postId)
                        )
                );
    }
}
