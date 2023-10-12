package net.maslyna.secutiry.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "t_tokens")
public class Token implements BaseEntity<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Column(unique = true, nullable = false)
    private String jwt;

    @OneToOne
    @JoinColumn(
            name = "account_id",
            referencedColumnName = "account_id",
            nullable = false
    )
    private Account account;
}
