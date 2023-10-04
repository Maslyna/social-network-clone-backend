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
    private List<User> subscriptions;

    @ManyToMany(mappedBy = "subscriptions")
    private List<User> followers;

    @Column(columnDefinition = "TRUE")
    private boolean enabledNotifications;

    public boolean addFollower(User user) {
        return followers.add(user);
    }

    public boolean removeFollower(User user) {
        return followers.remove(user);
    }

    public boolean addSubscribe(User user) {
        return subscriptions.add(user);
    }

    public boolean removeSubscribe(User user) {
        return subscriptions.remove(user);
    }

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
