package net.maslyna.post.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.maslyna.post.model.PostStatus;
import net.maslyna.post.model.dto.request.PostRequest;
import net.maslyna.post.model.entity.Hashtag;
import net.maslyna.post.model.entity.Post;
import net.maslyna.post.repository.HashtagRepository;
import net.maslyna.post.repository.PostRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PostService {
    private final PostRepository postRepository;
    private final HashtagRepository hashtagRepository;

    @Transactional(readOnly = true)
    public Page<Post> getAllPosts(PageRequest pageRequest) {
        return postRepository.findByStatus(PostStatus.PUBLISHED, pageRequest);
    }

    @Transactional(readOnly = true)
    public Page<Post> getPersonPosts(Long userId, PageRequest pageRequest) {
        return postRepository.findByUserIdAndStatus(userId, PostStatus.PUBLISHED, pageRequest);
    }

    @Transactional(readOnly = true)
    public Page<Post> getPrivatePosts(Long userId, PageRequest pageRequest) {
        return postRepository.findByUserId(userId, pageRequest);
    }

    public void createPost(Long userId, PostRequest request) {
        Post post = createNewPost(userId, request);
        log.info("post with id = {} created", post.getId());
    }

    private Post createNewPost(Long userId, PostRequest request) {
        return postRepository.save(
                Post.builder() // TODO: after creating file service add opportunity to upload images
                        .userId(userId)
                        .status(request.status())
                        .title(request.title())
                        .text(request.text())
                        .hashtags(createHashtagSet(request.hashtags()))
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
}
