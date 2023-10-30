package net.maslyna.user.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "t_photos")
public class Photo implements BaseEntity<UUID> {
    @Id
    private UUID id;

    @Column(length = 500)
    private String imageUrl;

    private Instant createdAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
