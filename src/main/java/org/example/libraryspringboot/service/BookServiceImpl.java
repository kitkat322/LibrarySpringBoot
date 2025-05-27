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

    @Override
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public List<Book> searchBooksByTitleOrAuthor(String query) {
        return bookRepository.findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(query, query);
    }

    @Override
    public Optional<Book> findById(int id) {
        return bookRepository.findById(id);
    }

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
    public void saveBook(Book book) {
        bookRepository.save(book);
    }






    @Override
    public Page<Book> getAllBooksPaginated(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return bookRepository.findAll(pageable);
    }

    @Override
    public Page<Book> searchBooksByTitleOrAuthor(String query, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return bookRepository.findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(query, query, pageable);
    }


    public List<Book> searchOrGetAll(String query) {
        if (query != null && !query.trim().isEmpty()) {
            return searchBooksByTitleOrAuthor(query);
        }
        return getAllBooks();
    }




//    public boolean reserveBook(Long bookId) {
//        Optional<Book> bookOptional = bookRepository.findById(bookId);
//        if (bookOptional.isPresent()) {
//            Book book = bookOptional.get();
//            if (book.isAvailable()) {
//                book.setAvailable(false); // или любое логическое поле
//                bookRepository.save(book);
//                return true;
//            }
//        }
//        return false;
//    }
}
