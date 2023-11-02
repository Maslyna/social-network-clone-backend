package net.maslyna.post.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.maslyna.common.model.FileType;
import net.maslyna.common.service.PropertiesMessageService;
import net.maslyna.post.exception.AccessDeniedException;
import net.maslyna.post.exception.NotFoundException;
import net.maslyna.post.exception.PostNotFoundException;
import net.maslyna.post.model.PostStatus;
import net.maslyna.post.model.dto.request.PostRequest;
import net.maslyna.post.model.entity.Hashtag;
import net.maslyna.post.model.entity.Photo;
import net.maslyna.post.model.entity.post.Post;
import net.maslyna.post.model.entity.post.RePost;
import net.maslyna.post.producer.KafkaProducer;
import net.maslyna.post.repository.HashtagRepository;
import net.maslyna.post.repository.PostRepository;
import net.maslyna.post.repository.RePostRepository;
import net.maslyna.post.service.PhotoService;
import net.maslyna.post.service.PostService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.*;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final RePostRepository rePostRepository;
    private final HashtagRepository hashtagRepository;
    private final KafkaProducer kafkaProducer;
    private final PropertiesMessageService messageService;
    private final PhotoService photoService;

    @Override
    @Transactional(readOnly = true)
    public Page<Post> getPosts(String[] hashtags, PageRequest pageRequest) {
        return hashtags != null
                ? postRepository.findPostsByStatusAndHashtags(hashtags, PostStatus.PUBLISHED, pageRequest)
                : postRepository.findByStatus(PostStatus.PUBLISHED, pageRequest);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Post> getPersonPosts(Long userId, Long authenticatedUserId, PageRequest pageRequest) {
        return !userId.equals(authenticatedUserId)
                ? getPublicPosts(userId, pageRequest)
                : getPrivatePosts(userId, pageRequest);
    }

    @Override
    @Transactional(readOnly = true)
    public Post getPost(Long authenticatedUserId, UUID postId) {
        Post post = getPostById(postId);

        if (!post.getStatus().equals(PostStatus.PUBLISHED)) {
            if (!post.getUserId().equals(authenticatedUserId)) {
                throw new AccessDeniedException(
                        FORBIDDEN,
                        messageService.getProperty("error.access.denied")
                );
            }
        }
        return post;
    }

    @Override
    public UUID createPost(Long userId, PostRequest request) {
        Post post = createNewPost(userId, request);

        log.info("post with id = {} created", post.getId());

        kafkaProducer.sendPostCreatedEvent(post);
        return post.getId();
    }

    @Override
    public UUID createRepost(Long userId, UUID postId, PostRequest request) {
        Post post = getPost(userId, postId);
        RePost rePost = createRePost(userId, request, post);

        log.info("repost with id = {} created", rePost.getId());

        kafkaProducer.sendPostCreatedEvent(rePost);
        return rePost.getId();
    }

    @Override
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

        //TODO: extract new method
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

    @Override
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

    @Override
    public String uploadPhoto(Long authenticatedUserId, UUID postId, MultipartFile file) {
        Post post = getPostById(postId);
        if (!post.getUserId().equals(authenticatedUserId)) {
            throw new AccessDeniedException(FORBIDDEN);
        }
        Photo photo = photoService.save(authenticatedUserId, FileType.POST_CONTENT, file);
        post.addPhoto(photo);
        return photo.getContentUrl();
    }


    @Override
    public void deletePhoto(Long authenticatedUserId, UUID postId, UUID photoId) {
        Post post = getPostById(postId);
        if (!post.getUserId().equals(authenticatedUserId)) {
            throw new AccessDeniedException(FORBIDDEN);
        }
        Photo photo = photoService.getPhotoById(photoId);
        post.removePhoto(photo);
        photoService.delete(authenticatedUserId, photo);
    }

    @Override
    public boolean isPostExists(UUID postId) {
        return postRepository.existsById(postId);
    }

    @Override
    public void checkIsPostExists(UUID postId) {
        if (!postRepository.existsById(postId)) {
            throw new NotFoundException(
                    NOT_FOUND,
                    messageService.getProperty("error.post.not-found", postId)
            );
        }
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
