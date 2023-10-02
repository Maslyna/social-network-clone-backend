package net.maslyna.post.model.entity.like;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import net.maslyna.post.model.entity.Post;


@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
@Entity
public class PostLike extends AbstractLike {
    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @Override
    public boolean remove() {
        return post.getLikes().remove(this);
    }
}