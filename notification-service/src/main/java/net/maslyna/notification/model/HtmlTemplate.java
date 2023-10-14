package net.maslyna.notification.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.maslyna.common.kafka.dto.PostCreatedEvent;
import net.maslyna.common.service.PropertiesMessageService;
import org.springframework.boot.SpringApplication;
import org.springframework.orm.hibernate5.SpringSessionContext;

@RequiredArgsConstructor
@Getter
public enum HtmlTemplate {

    POST_CREATED("template/post-created.html") {
        public String getSubject() {
            return "The user you follow has a new post";
        }
    },
    POST_LIKED("template/post-liked.html") {
        public String getSubject() {
            return "Someone liked your post";
        }
    },
    COMMENT_CREATED("template/comment-created.html"){
        public String getSubject() {
            return "Someone created a new comment";
        }
    },
    COMMENT_LIKED("template/comment-liked.html"){
        public String getSubject() {
            return "Someone liked your comment";
        }
    };

    private final String path;
    public abstract String getSubject();
}
