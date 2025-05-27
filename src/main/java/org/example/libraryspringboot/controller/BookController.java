package org.example.libraryspringboot.controller;

import lombok.RequiredArgsConstructor;
import org.example.libraryspringboot.entity.Book;
import org.example.libraryspringboot.entity.User;
import org.example.libraryspringboot.service.BookService;
import org.example.libraryspringboot.service.BookingService;
import org.example.libraryspringboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class BookController {

    @Autowired
    private final BookService bookService;

    @Autowired
    private final UserService userService;

    @Autowired
    private final BookingService bookingService;

    @GetMapping("/books")
    public String showBooks(@RequestParam(value = "query", required = false) String query,
                            @RequestParam(value = "page", defaultValue = "1") int page,
                            @AuthenticationPrincipal UserDetails userDetails,
                            Model model) {

        bookingService.updateExpiredBookings();

        // Получаем пользователя
        User user = null;
        if (userDetails != null) {
            user = userService.findByUsername(userDetails.getUsername());
            bookingService.updateBlockStatus(user); // ⬅️ Проверка блокировки
            model.addAttribute("isBlocked", user.isBlocked()); // Передаём в шаблон
        } else {
            model.addAttribute("isBlocked", false); // гость
        }

        // Получение книг
        List<Book> books = bookService.searchOrGetAll(query);
        model.addAttribute("books", books);

        // другие параметры пагинации, сообщений и т.д.
        return "public_book_list/book_list";
    }
}
