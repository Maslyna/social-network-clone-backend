package net.maslyna.post.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.maslyna.post.model.dto.request.PostRequest;
import net.maslyna.post.model.entity.Hashtag;
import net.maslyna.post.model.entity.Post;
import net.maslyna.post.repository.HashtagRepository;
import net.maslyna.post.repository.PostRepository;
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

    public void createPost(Long userId, PostRequest request) {
        Post post = postRepository.save(
                Post.builder() // TODO: after creating file service add opportunity to upload images
                        .userId(userId)
                        .status(request.status())
                        .title(request.title())
                        .text(request.text())
                        .hashtags(createHashtagSet(request.hashtags()))
                        .build()
        );
        log.info("post = {}", post);
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
