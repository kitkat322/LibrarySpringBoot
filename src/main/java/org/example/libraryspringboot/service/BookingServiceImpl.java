package org.example.libraryspringboot.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.libraryspringboot.entity.Book;
import org.example.libraryspringboot.entity.Booking;
import org.example.libraryspringboot.entity.User;
import org.example.libraryspringboot.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BookingServiceImpl implements BookingService {

    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private final BookService bookService;
    @Autowired
    private final UserService userService;

    @Override
    public List<Booking> getActiveBookings(String username) {
        return bookingRepository.findByUserUsernameAndStatus(username, Booking.Status.BOOKED);
    }

    @Override
    public void confirmIssue(int bookingId) {
        Booking booking = bookingRepository.findById(bookingId).orElse(null);
        if (booking != null && booking.getStatus() == Booking.Status.BOOKED) {
            booking.setStatus(Booking.Status.TAKEN);
            booking.setRentStartDate(LocalDateTime.now());
            // например, срок возврата через 14 дней:
            booking.setRentEndDate(LocalDateTime.now().plusMinutes(1)); //.plusDays(14));
            bookingRepository.save(booking);
        }
    }


    public void confirmReturn(int bookingId) {
        Booking booking = bookingRepository.findById(bookingId).orElse(null);
        if (booking != null && (booking.getStatus() == Booking.Status.TAKEN || booking.getStatus() == Booking.Status.EXPIRED)) {
            booking.setStatus(Booking.Status.RETURNED);
            booking.setRentEndDate(LocalDateTime.now()); // фиксируем возврат
            booking.setReturned(true);
            bookService.markBookAsAvailable(booking.getBook());
            bookingRepository.save(booking);
        }
    }


    public List<Booking> getUserBookingsByStatus(User user, Booking.Status status) {
        return bookingRepository.findByUserAndStatus(user, status);
    }

    public List<Booking> getUserOverdueRentals(User user) {
        return bookingRepository.findByUserAndStatusAndRentEndDateBefore(user, Booking.Status.EXPIRED, LocalDateTime.now());
    }



//    @Override
//    public List<Booking> getActiveRentals(String username) {
//        return bookingRepository.findByUserUsernameAndStatus(username, Booking.Status.TAKEN);
//    }

    @Override
    public List<Booking> getActiveRentals(String username) {
        return bookingRepository.findByUserUsernameAndStatusIn(
                username,
                List.of(Booking.Status.TAKEN, Booking.Status.EXPIRED)
        );
    }



    @Override
    public List<Booking> getReturnedBooks(String username) {
        return bookingRepository.findByUserUsernameAndStatusInOrderByBookingDateDesc(username, List.of(
                Booking.Status.RETURNED, Booking.Status.CANCELLED, Booking.Status.EXPIRED
        ));
    }

    @Override
    public boolean reserveBook(int bookId, String username) {
        Optional<Book> optionalBook = bookService.findById(bookId);
        User user = userService.findByUsername(username);

        if (optionalBook.isEmpty() || user == null) return false;

        Book book = optionalBook.get();
        if (!book.isAvailable()) return false;

        bookService.markBookAsUnavailable(book);

        Booking booking = new Booking();
        booking.setUser(user);
        booking.setBook(book);
        booking.setBookingDate(LocalDateTime.now());
        booking.setPickupDeadline(LocalDateTime.now().plusDays(1));
        booking.setStatus(Booking.Status.BOOKED);
        booking.setReturned(false);

        bookingRepository.save(booking);

        return true;
    }

    @Transactional
    @Override
    public void cancelBooking(int bookingId) {
        Optional<Booking> optional = bookingRepository.findById(bookingId);
        if (optional.isPresent()) {
            Booking booking = optional.get();
            if (booking.getStatus() == Booking.Status.BOOKED) {
                booking.setStatus(Booking.Status.CANCELLED);
                booking.setReturned(true);

                bookService.markBookAsAvailable(booking.getBook());
                bookingRepository.save(booking);
            }
        }
    }


    @Override
    public List<Booking> getAllActiveBookings() {
        return bookingRepository.findByStatus(Booking.Status.BOOKED);
    }

    @Override
    public List<Booking> getAllActiveRentals() {
        return bookingRepository.findByStatus(Booking.Status.TAKEN);
    }


    @Override
    public List<Booking> getOverdueBookings() {
        return bookingRepository.findOverdueBookings(LocalDate.now());
    }

    @Override
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    public boolean issueBookManually(int bookId, String username) {
        Book book = bookService.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Книга не найдена"));
        if (!book.isAvailable()) return false;

        User user = userService.findByUsername(username);
        if (user == null) {
            throw new RuntimeException("Пользователь не найден");
        }

        Booking booking = new Booking();
        booking.setBook(book);
        booking.setUser(user);
        booking.setBookingDate(LocalDateTime.now());
        booking.setStatus(Booking.Status.TAKEN);
        booking.setRentStartDate(LocalDateTime.now());
        booking.setRentEndDate(LocalDateTime.now().plusMinutes(1)); //.plusDays(14));

        book.setAvailable(false);
        //book.setAvailableFrom(null);

        bookingRepository.save(booking);
        bookService.saveBook(book);
        return true;
    }

    public void updateBlockStatus(User user) {
        List<Booking> overdueBookings = bookingRepository.findOverdueBookingsByUser(user.getId());
        boolean isOverdue = !overdueBookings.isEmpty();

        // Обновляем только если статус реально изменился
        if (user.isBlocked() != isOverdue) {
            user.setBlocked(isOverdue);
            userService.updateUser(user);  // 👈 безопасно, без перехеширования пароля
        }
    }

    public void updateExpiredBookings() {
        List<Booking> overdueBookings = bookingRepository.findAllByRentEndDateBeforeAndReturnedFalse(LocalDateTime.now());

        for (Booking booking : overdueBookings) {
            booking.setStatus(Booking.Status.EXPIRED);
        }

        bookingRepository.saveAll(overdueBookings);
    }
}
