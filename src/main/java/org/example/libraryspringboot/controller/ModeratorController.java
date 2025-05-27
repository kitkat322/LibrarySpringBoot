package org.example.libraryspringboot.controller;

import lombok.RequiredArgsConstructor;
import org.example.libraryspringboot.entity.Book;
import org.example.libraryspringboot.entity.Booking;
import org.example.libraryspringboot.entity.User;
import org.example.libraryspringboot.service.BookService;
import org.example.libraryspringboot.service.BookingService;
import org.example.libraryspringboot.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/moderator")
public class ModeratorController {

    private final BookingService bookingService;
    private final BookService bookService;
    private final UserService userService;

    @GetMapping("moderator_panel/moderator_panel")
    public String moderatorDashboard() {
        return "moderator/moderator_panel/moderator_panel";
    }

    @GetMapping("/moderator_user_operations/moderator_user_list")
    public String listUsers(@RequestParam(value = "username", required = false) String username, Model model) {
        List<User> users = (username != null && !username.isEmpty())
                ? userService.findUsersByUsernameContaining(username)
                : userService.getAllUsers();
        model.addAttribute("users", users);
        model.addAttribute("username", username);
        return "moderator/moderator_user_operations/moderator_user_list";
    }

    @GetMapping("/user/{userId}")
    public String userBookings(@PathVariable int userId, Model model) {
        bookingService.updateExpiredBookings();

        User user = userService.findUserById(userId);
        model.addAttribute("user", user);

        model.addAttribute("booked", bookingService.getUserBookingsByStatus(user, Booking.Status.BOOKED));
        model.addAttribute("taken", bookingService.getActiveRentals(user.getUsername()));
        model.addAttribute("overdue", bookingService.getUserOverdueRentals(user));
        model.addAttribute("returned", bookingService.getUserBookingsByStatus(user, Booking.Status.RETURNED));

        return "moderator/moderator_user_operations/user_bookings";
    }

    @PostMapping("/issue/confirm")
    public String confirmIssue(@RequestParam int bookingId, @RequestParam int userId) {
        bookingService.confirmIssue(bookingId);
        return "redirect:/moderator/user/" + userId;
    }

    @PostMapping("/book_return/confirm")
    public String confirmReturnFromUserPage(@RequestParam int bookingId, @RequestParam int userId) {
        bookingService.confirmReturn(bookingId);
        return "redirect:/moderator/user/" + userId;
    }

    @GetMapping("/book_operations/book_operations")
    public String bookOperations(@RequestParam(value = "search", required = false) String search, Model model) {
        List<Book> books = (search != null && !search.isBlank())
                ? bookService.searchBooksByTitleOrAuthor(search)
                : bookService.getAllBooks();
        model.addAttribute("books", books);
        model.addAttribute("search", search);
        return "moderator/book_operations/book_operations";
    }

    @GetMapping("/issue-books")
    public String showIssueBooks(@RequestParam int userId,
                                 @RequestParam(value = "search", required = false) String search,
                                 @ModelAttribute("messages") Map<Integer, String> messages,
                                 @ModelAttribute("messageTypes") Map<Integer, String> messageTypes,
                                 Model model) {
        User user = userService.findUserById(userId);
        List<Book> books = (search != null && !search.isBlank())
                ? bookService.searchBooksByTitleOrAuthor(search)
                : bookService.getAllBooks();

        model.addAttribute("books", books);
        model.addAttribute("search", search);
        model.addAttribute("userId", user.getId());
        model.addAttribute("username", user.getUsername());

        // Добавляем в модель сообщения (если есть)
        model.addAttribute("messages", messages);
        model.addAttribute("messageTypes", messageTypes);

        return "moderator/rental_operations/book_issue";
    }

    @PostMapping("/book/{id}/issue")
    public String issueBookToUser(@PathVariable int id,
                                  @RequestParam String username,
                                  @RequestParam int userId,
                                  @RequestParam(required = false) String search,
                                  Model model) {
        List<Book> books = (search != null && !search.isBlank())
                ? bookService.searchBooksByTitleOrAuthor(search)
                : bookService.getAllBooks();

        Map<Integer, String> messages = new HashMap<>();
        Map<Integer, String> messageTypes = new HashMap<>();

        try {
            boolean success = bookingService.issueBookManually(id, username);
            if (success) {
                messages.put(id, "Книга выдана пользователю: " + username);
                messageTypes.put(id, "success");
            } else {
                messages.put(id, "Не удалось выдать книгу. Возможно, она уже занята.");
                messageTypes.put(id, "error");
            }
        } catch (Exception e) {
            messages.put(id, "Ошибка: " + e.getMessage());
            messageTypes.put(id, "error");
        }

        model.addAttribute("books", books);
        model.addAttribute("search", search);
        model.addAttribute("userId", userId);
        model.addAttribute("username", username);
        model.addAttribute("messages", messages);
        model.addAttribute("messageTypes", messageTypes);

        return "moderator/rental_operations/book_issue";
    }
}