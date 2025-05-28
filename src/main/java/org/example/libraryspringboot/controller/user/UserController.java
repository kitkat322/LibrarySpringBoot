package org.example.libraryspringboot.controller.user;

import lombok.RequiredArgsConstructor;
import org.example.libraryspringboot.service.BookingService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final BookingService bookingService;

    @GetMapping("/user_panel")
    public String userAccountPage(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        String username = userDetails.getUsername();

        model.addAttribute("bookings", bookingService.getActiveBookings(username));
        model.addAttribute("activeRentals", bookingService.getActiveRentals(username));
        //model.addAttribute("expiredRentals", bookingService.getExpiredRentals(username));
        model.addAttribute("returnedBooks", bookingService.getReturnedBooks(username));

        return "user/user_panel/user_panel";
    }
}