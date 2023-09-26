package net.maslyna.post.model.entity;

import jakarta.persistence.*;
import lombok.*;
import net.maslyna.post.model.PostStatus;

import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Table(name = "t_posts")
public class Post implements BaseEntity<UUID> {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "post_id")
    private UUID id;

    private Long userId;

    @Enumerated(EnumType.STRING)
    private PostStatus status;

    private String title;

    private String text;

    @ManyToMany
    @JoinTable(
            name = "t_post_hashtags",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "hashtag_id")
    )
    private Set<Hashtag> hashtags;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Comment> comments;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Like> likes;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Post post = (Post) o;

        if (!Objects.equals(id, post.id)) return false;
        return Objects.equals(userId, post.userId);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", userId=" + userId +
                ", status=" + status +
                ", title='" + title + '\'' +
                ", text='" + text + '\'' +
                '}';
    }
}
