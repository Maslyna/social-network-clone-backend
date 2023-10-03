package net.maslyna.post.model.entity.like;

import net.maslyna.post.model.entity.BaseEntity;

import java.io.Serializable;
import java.time.Instant;

public interface Like<T extends Serializable> extends BaseEntity<T> {
    Long getUserId();

    void setUserId(Long userId);

    Instant getCreatedAt();

    void setCreatedAt(Instant createdAt);

    boolean remove();
}
