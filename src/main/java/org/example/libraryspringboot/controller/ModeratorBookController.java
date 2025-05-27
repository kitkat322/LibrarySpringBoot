package org.example.libraryspringboot.controller;

import lombok.RequiredArgsConstructor;
import org.example.libraryspringboot.entity.Book;
import org.example.libraryspringboot.service.BookService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/moderator/book")
@RequiredArgsConstructor
public class ModeratorBookController {

    private final BookService bookService;

    @GetMapping("/add")
    public String showAddForm(Model model) {
        return "moderator/book_operations/book_add";
    }

    @PostMapping("/add")
    public String addBook(@RequestParam String title,
                          @RequestParam String author,
                          @RequestParam String description) {
        bookService.saveBook(new Book(title, author, description));
        return "redirect:/moderator/book_operations/book_operations";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable int id, Model model) {
        Book book = bookService.findById(id)
                .orElseThrow(() -> new RuntimeException("Книга не найдена"));
        model.addAttribute("book", book);
        return "moderator/book_operations/book_edit";
    }

    @PostMapping("/edit")
    public String editBook(@ModelAttribute Book book) {
        bookService.updateBook(book);
        return "redirect:/moderator/book_operations/book_operations";
    }

    @PostMapping("/delete/{id}")
    public String deleteBook(@PathVariable int id) {
        bookService.deleteBook(id);
        return "redirect:/moderator/book_operations/book_operations";
    }

    @GetMapping("/{id}")
    public String viewBookDetails(@PathVariable int id, Model model) {
        Book book = bookService.findById(id)
                .orElseThrow(() -> new RuntimeException("Книга не найдена"));
        model.addAttribute("book", book);
        return "moderator/book_operations/book_details";
    }
}
