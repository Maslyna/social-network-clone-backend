package net.maslyna.post.model.entity.like;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import net.maslyna.post.model.entity.Comment;

@SuperBuilder
@NoArgsConstructor
@Getter
@Setter
@Entity
public class CommentLike extends AbstractLike {
    @ManyToOne
    @JoinColumn(name = "comment_id")
    private Comment comment;

    @Override
    public boolean remove() {
        return comment.getLikes().remove(this);
    }
}