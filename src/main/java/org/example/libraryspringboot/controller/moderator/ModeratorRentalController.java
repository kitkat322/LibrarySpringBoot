package org.example.libraryspringboot.controller.moderator;


import lombok.RequiredArgsConstructor;
import org.example.libraryspringboot.entity.Book;
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

@RequiredArgsConstructor
@Controller
public class ModeratorRentalController {

    private final UserService userService;
    private final BookService bookService;
    private final BookingService bookingService;



    //мануальная выдача книг через список книг
    @GetMapping("/moderator/issue-books")
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


    //запрос на подтверждение выдачи книги
    @PostMapping("/moderator/book/{id}/issue")
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

    //подтверждение запроса гет о подтверждении выдачи книги
    @PostMapping("/moderator/issue/confirm")
    public String confirmIssue(@RequestParam int bookingId, @RequestParam int userId) {
        bookingService.confirmIssue(bookingId);
        return "redirect:/moderator/user/" + userId;
    }


    //подтверждение запроса гет о возврате книги
    @PostMapping("/moderator/book_return/confirm")
    public String confirmReturnFromUserPage(@RequestParam int bookingId, @RequestParam int userId) {
        bookingService.confirmReturn(bookingId);
        return "redirect:/moderator/user/" + userId;
    }
}
