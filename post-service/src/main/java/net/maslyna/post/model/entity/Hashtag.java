package net.maslyna.post.model.entity;

import jakarta.persistence.*;
import lombok.*;
import net.maslyna.post.model.entity.post.Post;

import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "t_hashtags")
public class Hashtag implements BaseEntity<UUID> {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "hashtag_id" , nullable = false)
    private UUID id;

    private String text;

    @ManyToMany(mappedBy = "hashtags")
    private Set<Post> posts;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Hashtag hashtag = (Hashtag) o;

        if (!Objects.equals(id, hashtag.id)) return false;
        return Objects.equals(text, hashtag.text);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (text != null ? text.hashCode() : 0);
        return result;
    }
}
