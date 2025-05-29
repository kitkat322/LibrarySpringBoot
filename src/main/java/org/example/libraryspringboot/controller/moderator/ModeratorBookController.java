package org.example.libraryspringboot.controller.moderator;

import lombok.RequiredArgsConstructor;
import org.example.libraryspringboot.entity.Book;
import org.example.libraryspringboot.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/moderator/book")
@RequiredArgsConstructor
public class ModeratorBookController {

    @Autowired
    private final BookService bookService;

    @GetMapping("/book_list")
    public String showModeratorBookList(@RequestParam(value = "search", required = false) String search, Model model) {
        List<Book> books = bookService.searchOrGetAll(search);
//                (search != null && !search.isBlank())
//                ? bookService.searchBooksByTitleOrAuthor(search)
//                : bookService.getAllBooks();
        model.addAttribute("books", books);
        model.addAttribute("search", search);
        return "moderator/book_operations/book_operations";
    }

    @GetMapping("/{id}")
    public String viewBookDetails(@PathVariable int id, Model model) {
        Book book = bookService.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found with id: " + id));
        model.addAttribute("book", book);
        return "moderator/book_operations/book_details";
    }

    @GetMapping("/add")
    public String showAddBookForm(Model model) {
        return "moderator/book_operations/book_add";
    }

    @PostMapping("/add")
    public String addBook(@RequestParam String title,
                          @RequestParam String author,
                          @RequestParam String description) {
        Book book = new Book(title, author, description);
        bookService.saveBook(book);
        return "redirect:/moderator/book/"  + book.getId();
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable int id, Model model) {
        Book book = bookService.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found with id: " + id));
        model.addAttribute("book", book);
        return "moderator/book_operations/book_edit";
    }

    @PostMapping("/edit")
    public String editBook(@ModelAttribute Book book) {
        bookService.updateBook(book);
        return "redirect:/moderator/book/"  + book.getId();
    }

    @PostMapping("/delete/{id}")
    public String deleteBook(@PathVariable int id) {
        bookService.deleteBook(id);
        return "redirect:/moderator/book/book_list";
    }
}
