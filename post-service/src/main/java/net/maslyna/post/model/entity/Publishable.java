package net.maslyna.post.model.entity;

import java.io.Serializable;
import java.time.Instant;

/**
 * Used for all entities, that user can post.
 * @param <T> Serializable type of value.
 * @author Maslyna
 */
public interface Publishable<T extends Serializable> extends BaseEntity<T> {
    Long getUserId();

    Instant getCreatedAt();

}
