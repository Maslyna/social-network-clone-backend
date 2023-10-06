package net.maslyna.secutiry.model.entity;

import jakarta.persistence.*;
import lombok.*;
import net.maslyna.secutiry.model.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

@Entity
@Table(name = "t_accounts")
public class Account implements BaseEntity<Long>, UserDetails {
    @Id
    @Column(nullable = false, name = "account_id")
    private Long id;

    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private Role role;

    private boolean isAccountNonLocked;

    private boolean isEnabled;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Set.of(role);
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
}
