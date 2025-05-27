package org.example.libraryspringboot.controller.moderator;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
@RequiredArgsConstructor
public class ModeratorController {

    @GetMapping("/moderator/moderator_panel")
    public String moderatorDashboard() {
        return "moderator/moderator_panel/moderator_panel";
    }
}