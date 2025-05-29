package org.example.libraryspringboot.controller.user;

import lombok.RequiredArgsConstructor;
import org.example.libraryspringboot.entity.User;
import org.example.libraryspringboot.service.BookingService;
import org.example.libraryspringboot.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class UserBookingController {

    private final BookingService bookingService;
    private final UserService userService;

    @PostMapping("/user/booking/book/{id}/reserve")
    public String reserveBook(@PathVariable int id,
                              @AuthenticationPrincipal UserDetails userDetails,
                              RedirectAttributes redirectAttributes) {

        User user = userService.findByUsername(userDetails.getUsername());
        bookingService.updateBlockStatus(user);
        String username = userDetails.getUsername();
        boolean success = bookingService.reserveBook(id, username);

        Map<Integer, String> messages = new HashMap<>();
        Map<Integer, String> messageTypes = new HashMap<>();

        messages.put(id, success ? "Booking is successful" : "Booking is failed. Book is already taken.");
        messageTypes.put(id, success ? "success" : "error");

        redirectAttributes.addFlashAttribute("messages", messages);
        redirectAttributes.addFlashAttribute("messageTypes", messageTypes);

        return "redirect:/catalog/books";
    }

    @PostMapping("/user/booking/{bookingId}/cancel")
    public String cancelBooking(@PathVariable("bookingId") int bookingId) {
        bookingService.cancelBooking(bookingId);
        return "redirect:/user/user_panel";
    }
}
