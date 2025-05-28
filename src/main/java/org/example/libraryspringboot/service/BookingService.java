package org.example.libraryspringboot.service;

import org.example.libraryspringboot.entity.Booking;
import org.example.libraryspringboot.entity.User;

import java.util.List;

public interface BookingService {

    List<Booking> getActiveBookings(String username);
    List<Booking> getActiveRentals(String username);
    List<Booking> getReturnedBooks(String username);
    boolean reserveBook(int bookId, String username);
    void cancelBooking(int bookingId);

    List<Booking> getAllActiveBookings();// Ожидают выдачи
    void confirmIssue(int bookingId);


    List<Booking> getAllActiveRentals();// Уже выданы
    void confirmReturn(int bookingId);
        List<Booking> getOverdueBookings();     // Просроченные
        List<Booking> getAllBookings();         // Все бронирования


    List<Booking> getUserBookingsByStatus(User user, Booking.Status status);
    List<Booking> getUserOverdueRentals(User user);

    //boolean issueBookManually(int bookId, String username);
    boolean issueBookManually(int bookId, int userId);
    void updateBlockStatus(User user);

    void updateExpiredBookings();


}






