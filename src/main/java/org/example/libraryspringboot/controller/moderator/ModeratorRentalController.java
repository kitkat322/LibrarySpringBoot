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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Controller
public class ModeratorRentalController {

    private final UserService userService;
    private final BookService bookService;
    private final BookingService bookingService;

    //запрос на получение страницы выдачи книг через список книг
    @GetMapping("/moderator/rental/user/{userId}/book_issue_list")
    public String showIssueBooks(@PathVariable int userId,
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

        return "moderator/rental_operations/book_issue_list";
    }

    //запрос на подтверждение выдачи книги на странице со списком книг
    @PostMapping("/moderator/rental/user/{userId}/book_issue_list/book/{id}/issue_confirm")
    public String issueBookToUser(@PathVariable int id,
                                  @PathVariable int userId,
                                  @RequestParam(required = false) String search,
                                  RedirectAttributes redirectAttributes) {

        Map<Integer, String> messages = new HashMap<>();
        Map<Integer, String> messageTypes = new HashMap<>();
        User user = userService.findUserById(userId);

        try {
            boolean success = bookingService.issueBookManually(id, userId);
            if (success) {
                messages.put(id, "Книга выдана пользователю: " + user.getUsername());
                messageTypes.put(id, "success");
            } else {
                messages.put(id, "Не удалось выдать книгу. Возможно, она уже занята.");
                messageTypes.put(id, "error");
            }
        } catch (Exception e) {
            messages.put(id, "Ошибка: " + e.getMessage());
            messageTypes.put(id, "error");
        }

        redirectAttributes.addFlashAttribute("messages", messages);
        redirectAttributes.addFlashAttribute("messageTypes", messageTypes);

        redirectAttributes.addAttribute("userId", userId);
        if (search != null && !search.isBlank()) {
            redirectAttributes.addAttribute("search", search);
        }
        return "redirect:/moderator/rental/user/{userId}/book_issue_list";
    }

    //подтверждение запроса гет о подтверждении выдачи книги
    @PostMapping("/moderator/rental/user/{userId}/book_issue/booking{bookingId}/confirm")
    public String confirmIssue(@PathVariable int bookingId, @PathVariable int userId) {
        bookingService.confirmIssue(bookingId);
        return "redirect:/moderator/user/{userId}";
    }

    //подтверждение запроса гет о возврате книги
    @PostMapping("/moderator/rental/user/{userId}/book_return/booking{bookingId}/confirm")
    public String confirmReturnFromUserPage(@PathVariable int bookingId, @PathVariable int userId) {
        bookingService.confirmReturn(bookingId);
        return "redirect:/moderator/user/{userId}";
    }
}
