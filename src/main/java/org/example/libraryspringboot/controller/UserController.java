package org.example.libraryspringboot.controller;

import lombok.RequiredArgsConstructor;
import org.example.libraryspringboot.entity.User;
import org.example.libraryspringboot.service.BookingService;
import org.example.libraryspringboot.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
@RequiredArgsConstructor
public class UserController {

    private final BookingService bookingService;
    private final UserService userService;

    @GetMapping("/user/user_panel")
    public String userAccountPage(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        String username = userDetails.getUsername();

        model.addAttribute("bookings", bookingService.getActiveBookings(username));
        model.addAttribute("activeRentals", bookingService.getActiveRentals(username));
        //model.addAttribute("expiredRentals", bookingService.getExpiredRentals(username));


        model.addAttribute("returnedBooks", bookingService.getReturnedBooks(username));

        return "user/user_panel/user_panel";
    }
}