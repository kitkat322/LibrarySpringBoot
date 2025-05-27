package org.example.libraryspringboot.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String username;
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('USER', 'MODERATOR', 'ADMIN') default 'USER'")
    private Role role;

    private boolean isBlocked = false;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Booking> bookings;

    public enum Role {
        USER, MODERATOR, ADMIN
    }
}
