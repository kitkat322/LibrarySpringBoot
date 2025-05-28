package org.example.libraryspringboot.controller.utility;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    @GetMapping("/")
    public String showFirstView(){
        return "index";
    }
}
