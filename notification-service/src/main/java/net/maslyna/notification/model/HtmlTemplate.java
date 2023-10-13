package net.maslyna.notification.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.maslyna.common.kafka.dto.PostCreatedEvent;

@RequiredArgsConstructor
@Getter
public enum HtmlTemplate {

    POST_CREATED("template/post-created.html"),
    POST_LIKED("template/post-liked.html"),
    COMMENT_CREATED("template/comment-created.html"),
    COMMENT_LIKED("template/comment-liked.html");

    private final String path;
}
