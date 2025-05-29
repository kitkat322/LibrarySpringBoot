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

    //shows moderator's view of bookList
    @GetMapping("/moderator/rental/user/{userId}/book_issue_list")
    public String showManualBookIssueList(@PathVariable int userId,
                                          @RequestParam(value = "search", required = false) String search,
                                          @ModelAttribute("messages") Map<Integer, String> messages,
                                          @ModelAttribute("messageTypes") Map<Integer, String> messageTypes,
                                          Model model) {
        User user = userService.findUserById(userId);
        List<Book> books = bookService.searchOrGetAll(search);
//                (search != null && !search.isBlank())
//                ? bookService.searchBooksByTitleOrAuthor(search)
//                : bookService.getAllBooks();

        model.addAttribute("books", books);
        model.addAttribute("search", search);
        model.addAttribute("userId", user.getId());
        model.addAttribute("username", user.getUsername());

        // Добавляем в модель сообщения (если есть)
        model.addAttribute("messages", messages);
        model.addAttribute("messageTypes", messageTypes);

        return "moderator/rental_operations/book_issue_list";
    }

    //confirmation of book return via book ID
    @PostMapping("/moderator/rental/user/{userId}/book_issue_list/book/{id}/issue_confirm")
    public String confirmIssueFromManualBookIssueList(@PathVariable int id,
                                                      @PathVariable int userId,
                                                      @RequestParam(required = false) String search,
                                                      RedirectAttributes redirectAttributes) {

        Map<Integer, String> messages = new HashMap<>();
        Map<Integer, String> messageTypes = new HashMap<>();
        User user = userService.findUserById(userId);

        try {
            boolean success = bookingService.issueBookManually(id, userId);
            if (success) {
                messages.put(id, "The book has been issued to the user: " + user.getUsername());
                messageTypes.put(id, "success");
            } else {
                messages.put(id, "Failed to issue the book. It may already be taken.");
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

    //confirmation of book issue via booking ID
    @PostMapping("/moderator/rental/user/{userId}/book_issue/booking{bookingId}/confirm")
    public String confirmIssueFromUserPage(@PathVariable int bookingId,
                                           @PathVariable int userId) {
        bookingService.confirmIssue(bookingId);
        return "redirect:/moderator/user/{userId}";
    }

    //confirmation of book return via booking ID
    @PostMapping("/moderator/rental/user/{userId}/book_return/booking{bookingId}/confirm")
    public String confirmReturnFromUserPage(@PathVariable int bookingId,
                                            @PathVariable int userId) {
        bookingService.confirmReturn(bookingId);
        return "redirect:/moderator/user/{userId}";
    }
}
