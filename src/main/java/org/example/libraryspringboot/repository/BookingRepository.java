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

    //get all user`s bookings with a particular status
    List<Booking> findByUserUsernameAndStatus(String username, Booking.Status status);//Status

    //get all user`s bookings with particular statuses (in the list) ORDER BY rent_end_date DESC
    List<Booking> findByUserUsernameAndStatusInOrderByBookingDateDesc(String username, List<Booking.Status> statuses);


    //get all user`s rentals with a particular status ORDER BY rent_end_date DESC
    List<Booking> findByUserAndStatusOrderByRentEndDateDesc(User user, Booking.Status status);

    //get all user`s rentals with a particular status and which rent_end_date has expired ORDER BY rent_end_date DESC;
    List<Booking> findByUserAndStatusAndRentEndDateBeforeOrderByRentEndDateDesc(User user, Booking.Status status, LocalDateTime dateTime);

    //get all user`s rentals which rent_end_date is expired and which are not returned in the library
    @Query("SELECT b FROM Booking b WHERE b.user.id = :userId AND b.returned = false AND b.rentEndDate < CURRENT_TIMESTAMP")
    List<Booking> findOverdueBookingsByUser(@Param("userId") int userId);

    //get all rentals of all users which rent_end_date has expired, and they are not returned in the library
    List<Booking> findAllByRentEndDateBeforeAndReturnedFalse(LocalDateTime dateTime);

    //get all user`s rentals with particular statuses (in the list) ORDER BY rent_end_date DESC
    List<Booking> findByUserUsernameAndStatusInOrderByRentEndDateDesc(String username, List<Booking.Status> statuses);//StatusIn


}
