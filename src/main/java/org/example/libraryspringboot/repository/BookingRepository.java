package org.example.libraryspringboot.repository;

import org.example.libraryspringboot.entity.Booking;
import org.example.libraryspringboot.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {

    // Возвращает список бронирований по конкретному статусу
    List<Booking> findByUserUsernameAndStatus(String username, Booking.Status status);//Status


    //Возвращает бронирования, у которых статус входит в список
    List<Booking> findByUserUsernameAndStatusInOrderByBookingDateDesc(String username, List<Booking.Status> statuses);

    List<Booking> findByStatus(Booking.Status status);

    @Query("SELECT b FROM Booking b WHERE b.status = 'ACTIVE' AND b.rentEndDate < :today")
    List<Booking> findOverdueBookings(@Param("today") LocalDate today);

    List<Booking> findByUserAndStatusOrderByRentEndDateDesc(User user, Booking.Status status);

    List<Booking> findByUserAndStatusAndRentEndDateBeforeOrderByRentEndDateDesc(User user, Booking.Status status, LocalDateTime dateTime);

    @Query("SELECT b FROM Booking b WHERE b.user.id = :userId AND b.returned = false AND b.rentEndDate < CURRENT_DATE")
    List<Booking> findOverdueBookingsByUser(@Param("userId") int userId);

    List<Booking> findAllByRentEndDateBeforeAndReturnedFalse(LocalDateTime dateTime);

    List<Booking> findByUserUsernameAndStatusInOrderByRentEndDateDesc(String username, List<Booking.Status> statuses);//StatusIn


}
