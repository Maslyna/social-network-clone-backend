package net.maslyna.post.model.entity.post;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import net.maslyna.post.model.entity.Comment;
import net.maslyna.post.model.entity.Hashtag;
import net.maslyna.post.model.entity.Photo;
import net.maslyna.post.model.entity.like.PostLike;

import java.util.HashSet;
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
    private Set<Hashtag> hashtags = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Comment> comments = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PostLike> likes = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Photo> photos = new HashSet<>();

    public boolean addLike(PostLike like) {
        return likes.add(like);
    }

    public boolean addComment(Comment comment) {
        return comments.add(comment);
    }

    public boolean addPhoto(Photo photo) {
        return photos.add(photo);
    }

    public boolean removePhoto(Photo photo) {
        return photos.remove(photo);
    }
}
