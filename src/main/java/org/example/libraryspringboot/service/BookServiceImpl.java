package org.example.libraryspringboot.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.libraryspringboot.entity.Book;
import org.example.libraryspringboot.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {

    @Autowired
    BookRepository bookRepository;

    //method to find a Book by id
    @Override
    public Optional<Book> findById(int id) {
        return bookRepository.findById(id);
    }

    //main operations with a Book
    @Override
    public void saveBook(Book book) {
        bookRepository.save(book);
    }

    @Override
    public void updateBook(Book book) {
        Optional<Book> existingBookOpt = bookRepository.findById(book.getId());
        if (existingBookOpt.isPresent()) {
            Book existingBook = existingBookOpt.get();
            existingBook.setTitle(book.getTitle());
            existingBook.setAuthor(book.getAuthor());
            existingBook.setDescription(book.getDescription());
            // Добавь сюда другие поля, если есть

            bookRepository.save(existingBook);
        } else {
            throw new EntityNotFoundException("Книга с id " + book.getId() + " не найдена");
        }
    }

    @Override
    public void deleteBook(int id) {
        Optional<Book> bookOpt = bookRepository.findById(id);
        if (bookOpt.isPresent()) {
            Book book = bookOpt.get();
            if (book.isAvailable()) {
                bookRepository.deleteById(id);
            } else {
                throw new IllegalStateException("Нельзя удалить книгу, которая сейчас находится в аренде.");
            }
        } else {
            throw new EntityNotFoundException("Книга с id " + id + " не найдена");
        }
    }

    //methods to change book availability
    @Override
    public void markBookAsUnavailable(Book book) {
        book.setAvailable(false);
        bookRepository.save(book);
    }

    @Override
    public void markBookAsAvailable(Book book) {
        book.setAvailable(true);
        bookRepository.save(book);
    }

    //methods to find Lists of Books
    @Override
    public List<Book> searchOrGetAll(String query) {
        if (query != null && !query.trim().isEmpty()) {
            return searchBooksByTitleOrAuthor(query);
        }
        return getAllBooks();
    }

    //methods for method List<Book> searchOrGetAll(String query)
    @Override
    public List<Book> searchBooksByTitleOrAuthor(String query) {
        return bookRepository.findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(query, query);
    }

    @Override
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    //method to make pageable list of books
    @Override
    public Page<Book> getAllBooksPaginated(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return bookRepository.findAll(pageable);
    }
}
