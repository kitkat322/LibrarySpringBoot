package org.example.libraryspringboot.controller.admin;

import lombok.RequiredArgsConstructor;
import org.example.libraryspringboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/admin/user")
@Controller
public class AdminUserController {

    @Autowired
    private final UserService userService;

    @GetMapping("/admin_user_list")
    public String showUsers(@RequestParam(required = false) String username, Model model) {
        if (username != null && !username.isEmpty()) {
            //model.addAttribute("users", userService.findUsersByUsername(username));
            model.addAttribute("users", userService.findUsersByUsernameContaining(username));

        } else {
            model.addAttribute("users", userService.getAllUsers());
        }
        model.addAttribute("username", username); // Чтобы сохранить введённый текст в поле поиска
        return "admin/admin_user_operations/admin_user_list";
    }

    @PostMapping("/admin_user_list/user/{userId}/delete")
    public String deleteUser(@PathVariable int userId) {
        userService.deleteUserById(userId);
        return "redirect:/admin/user/admin_user_list";
    }

    @PostMapping("/admin_user_list/user/{userId}promote")
    public String promoteToModerator(@PathVariable int userId) {
        userService.changeUserRoleToModerator(userId);
        return "redirect:/admin/user/admin_user_list";
    }

    @PostMapping("/admin_user_list/user/{userId}/demote/")
    public String demoteToUser(@PathVariable int userId) {
        userService.changeUserRoleToUser(userId);
        return "redirect:/admin/user/admin_user_list";
    }
}