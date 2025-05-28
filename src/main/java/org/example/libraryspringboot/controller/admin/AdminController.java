package org.example.libraryspringboot.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    @GetMapping("/admin_panel")
    public String adminDashboard() {
        return "admin/admin_panel/admin_panel"; // admin/moderator_panel.html
    }
}
