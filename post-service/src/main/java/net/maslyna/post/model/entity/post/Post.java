package net.maslyna.post.model.entity.post;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import net.maslyna.post.model.entity.Comment;
import net.maslyna.post.model.entity.Hashtag;
import net.maslyna.post.model.entity.like.PostLike;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder

@Entity
@Table(name = "t_posts")
public class Post extends AbstractPost {
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
    private Set<PostLike> likes;

    public boolean addLike(PostLike like) {
        return likes.add(like);
    }

    public boolean addComment(Comment comment) {
        return comments.add(comment);
    }

}
