package net.maslyna.post.model.entity;

import jakarta.persistence.*;
import lombok.*;
import net.maslyna.post.model.CommentStatus;

import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "t_comments")
public class Comment implements BaseEntity<UUID> {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "comment_id", nullable = false)
    private UUID id;

    private Long userId;

    private String text;

    private CommentStatus status;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Comment comment = (Comment) o;

        if (!Objects.equals(id, comment.id)) return false;
        return Objects.equals(userId, comment.userId);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        return result;
    }
}
