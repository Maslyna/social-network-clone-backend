package net.maslyna.follower.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter

@Entity
@Table(name = "t_followers")
public class User implements BaseEntity<Long> {
    @Id
    @Column(name = "user_id")
    private Long id;

    @ManyToMany
    @JoinTable(
            name = "user_followers",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "follower_id")
    )
    private List<User> followedAt;

    @ManyToMany(mappedBy = "followedAt")
    private List<User> followers;

    @Column(columnDefinition = "TRUE")
    private boolean enabledNotifications;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return id.equals(user.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
