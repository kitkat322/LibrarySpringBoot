package org.example.libraryspringboot.service;

import org.example.libraryspringboot.entity.Book;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface BookService {

    //method to find a Book by id
    Optional<Book> findById(int id);

    //main operations with a Book
    void saveBook(Book book);
    void updateBook(Book book);
    void deleteBook(int id);

    //methods to change book availability
    void markBookAsAvailable(Book book);
    void markBookAsUnavailable(Book book);

    //methods to find Lists of Books
    List<Book> searchOrGetAll(String query);
    List<Book> searchBooksByTitleOrAuthor(String query);
    List<Book> getAllBooks();
    Page<Book> getAllBooksPaginated(int page, int size);
}
