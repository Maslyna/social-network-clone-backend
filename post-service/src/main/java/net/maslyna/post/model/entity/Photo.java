package net.maslyna.post.model.entity;


import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Table(name = "t_photos")
public class Photo implements BaseEntity<UUID> {

    @Id
    private UUID id;

    @Column(nullable = false)
    private Long userId;

    @Column(length = 500)
    private String contentUrl;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Photo photo = (Photo) o;

        if (!id.equals(photo.id)) return false;
        return userId.equals(photo.userId);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + userId.hashCode();
        return result;
    }
}
