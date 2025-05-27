package org.example.libraryspringboot.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(name = "bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    private LocalDateTime bookingDate;
    private LocalDateTime pickupDeadline;
    private LocalDateTime rentStartDate;
    private LocalDateTime rentEndDate;
    private boolean returned = false;

    @Enumerated(EnumType.STRING)
    private Status status;

    public enum Status {
        BOOKED, TAKEN, RETURNED, CANCELLED, EXPIRED
    }
}



















