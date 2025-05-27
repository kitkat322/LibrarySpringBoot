package org.example.libraryspringboot.entity;

import jakarta.persistence.*;
import lombok.*;


import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(name = "books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String title;

    private String author;

    @Column(length = 1000)
    private String description;

    @Column(name = "is_available")
    private boolean isAvailable = true;



    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL)
    private List<Booking> bookings;

    public Book(String title, String author, String description) {
        this.title = title;
        this.author = author;
        this.description = description;
    }
}
