package net.maslyna.user.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

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

    @Column(length = 50)
    private String name;

    private Instant createdAt;

    @Column(length = 500)
    private String imageUrl;

    private Instant birthday;
}
