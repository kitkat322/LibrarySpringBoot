package org.example.libraryspringboot.controller;


import lombok.RequiredArgsConstructor;
import org.example.libraryspringboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    UserService userService;

    @GetMapping
    public String adminDashboard() {
        return "admin/admin_panel/admin_panel"; // admin/moderator_panel.html
    }

    @GetMapping("/admin_user_operations/admin_user_list")
    public String showUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "admin/admin_user_operations/admin_user_list"; // admin/moderator_user_list.html
    }

    @PostMapping("/admin_user_operations/admin_user_list/delete/{id}")
    public String deleteUser(@PathVariable int id) {
        userService.deleteUserById(id);
        return "redirect:/admin/admin_user_operations/admin_user_list";
    }

    @PostMapping("/admin_user_operations/admin_user_list/promote/{id}")
    public String promoteToModerator(@PathVariable int id) {
        userService.changeUserRoleToModerator(id);
        return "redirect:/admin/admin_user_operations/admin_user_list";
    }

    @PostMapping("/admin_user_operations/admin_user_list/demote/{id}")
    public String demoteToUser(@PathVariable int id) {
        userService.changeUserRoleToUser(id);
        return "redirect:/admin/admin_user_operations/admin_user_list";
    }
}
