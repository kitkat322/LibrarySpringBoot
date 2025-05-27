package org.example.libraryspringboot.service;

import org.example.libraryspringboot.entity.Book;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface BookService {

    List<Book> getAllBooks();
    List<Book> searchBooksByTitleOrAuthor(String query);
    Optional<Book> findById(int id);
    void markBookAsAvailable(Book book);
    void markBookAsUnavailable(Book book);
    void deleteBook(int id);
    void updateBook(Book book);
    void saveBook(Book book);
    Page<Book> getAllBooksPaginated(int page, int size);
    Page<Book> searchBooksByTitleOrAuthor(String query, int page, int size);
    List<Book> searchOrGetAll(String query);
}
