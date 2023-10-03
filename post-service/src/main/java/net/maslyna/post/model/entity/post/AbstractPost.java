package net.maslyna.post.model.entity.post;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import net.maslyna.post.model.PostStatus;
import net.maslyna.post.model.entity.Publishable;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public abstract class AbstractPost implements Publishable<UUID> {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "post_id")
    private UUID id;

    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20)
    private PostStatus status;

    private Instant createdAt;

    @Column(name = "title", length = 100)
    private String title;

    @Column(name = "text", length = 500)
    private String text;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AbstractPost post = (AbstractPost) o;

        if (!Objects.equals(id, post.id)) return false;
        return Objects.equals(userId, post.userId);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        return result;
    }
}
