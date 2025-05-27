package org.example.libraryspringboot.controller.moderator;

import lombok.RequiredArgsConstructor;
import org.example.libraryspringboot.entity.Booking;
import org.example.libraryspringboot.entity.User;
import org.example.libraryspringboot.service.BookingService;
import org.example.libraryspringboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/moderator/user")
@Controller
public class ModeratorUserController {

    @Autowired
    private final UserService userService;
    @Autowired
    private final BookingService bookingService;

    @GetMapping("/user_list")
    public String listUsers(@RequestParam(value = "username", required = false) String username, Model model) {
        List<User> users = (username != null && !username.isEmpty())
                ? userService.findUsersByUsernameContaining(username)
                : userService.getAllUsers();
        model.addAttribute("users", users);
        model.addAttribute("username", username);
        return "moderator/moderator_user_operations/moderator_user_list";
    }

    @GetMapping("/{userId}")
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
}
