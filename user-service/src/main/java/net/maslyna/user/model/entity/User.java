package net.maslyna.user.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "t_users")
public class User implements BaseEntity<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false, unique = true)
    private Long id;

    @Column(unique = true, length = 100)
    private String email;

    @Column(length = 100)
    private String nickname;

    @Column(length = 100)
    private String name;

    @Column(length = 1000)
    private String bio;

    @Column(length = 200)
    private String location;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_photo_id")
    private Photo profilePhoto;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Photo> userPhotos = new ArrayList<>();

    private Instant birthday;

    private Instant createdAt;

    private boolean isPublicAccount;

    public boolean addPhoto(Photo photo) {
        return userPhotos.add(photo);
    }

    public boolean removePhoto(Photo photo) {
        return userPhotos.remove(photo);
    }

}
