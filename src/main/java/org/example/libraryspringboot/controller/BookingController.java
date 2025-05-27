package org.example.libraryspringboot.controller;

import lombok.RequiredArgsConstructor;
import org.example.libraryspringboot.service.BookingService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping("/books/{id}/reserve")
    public String reserveBook(@PathVariable int id,
                              @AuthenticationPrincipal UserDetails userDetails,
                              RedirectAttributes redirectAttributes) {
        String username = userDetails.getUsername();
        boolean success = bookingService.reserveBook(id, username);

        Map<Integer, String> messages = new HashMap<>();
        Map<Integer, String> messageTypes = new HashMap<>();

        messages.put(id, success ? "Книга успешно забронирована." : "Ошибка бронирования книги.");
        messageTypes.put(id, success ? "success" : "error");

        redirectAttributes.addFlashAttribute("messages", messages);
        redirectAttributes.addFlashAttribute("messageTypes", messageTypes);

        return "redirect:/books";
    }

    @PostMapping("/cancel-booking")
    public String cancelBooking(@RequestParam("bookingId") int bookingId) {
        bookingService.cancelBooking(bookingId);
        return "redirect:/user/account";
    }
}
