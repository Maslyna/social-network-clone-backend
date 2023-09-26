package net.maslyna.post.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Entity
public class Post implements BaseEntity<Long> {

    @Id
    private Long id;
    private Long userId;
    private String title;
}
