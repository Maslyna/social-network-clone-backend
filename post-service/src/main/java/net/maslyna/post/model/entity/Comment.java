package net.maslyna.post.model.entity;

import jakarta.persistence.*;
import lombok.*;
import net.maslyna.post.model.CommentStatus;
import net.maslyna.post.model.entity.like.CommentLike;
import net.maslyna.post.model.entity.post.Post;

import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "t_comments")
public class Comment implements Publishable<UUID> {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "comment_id", nullable = false)
    private UUID id;

    private Long userId;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "subcomment_on_comment_id")
    private Comment comment;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Comment> comments = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CommentLike> likes = new HashSet<>();

    private String text;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private Photo photo;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 10)
    private CommentStatus status;

    private Instant createdAt;

    public boolean addComment(Comment comment) {
        return comments.add(comment);
    }

    public boolean addLike(CommentLike like) {
        return likes.add(like);
    }

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
