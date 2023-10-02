package net.maslyna.post.model.entity.post;

import net.maslyna.post.model.PostStatus;
import net.maslyna.post.model.entity.BaseEntity;

import java.io.Serializable;
import java.time.Instant;

//TODO: maybe, I should use Publishable for all user-controlled entities
public interface Publishable<T extends Serializable> extends BaseEntity<T> {
    Long getUserId();

    void setUserId(Long id);

    PostStatus getStatus();

    void setStatus(PostStatus status);

    Instant getCreatedAt();

    void setCreatedAt(Instant createdAt);

    String getTitle();

    void setTitle(String title);

    String getText();

    void setText(String text);
}
