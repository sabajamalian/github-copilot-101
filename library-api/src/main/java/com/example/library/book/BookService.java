package com.example.library.book;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class BookService {

    private static final int LOAN_PERIOD_DAYS = 14;

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> findBooks() {
        return bookRepository.findAll();
    }

    public Book findBook(Long id) {
        return bookRepository.findById(id).orElse(null);
    }

    public Book saveBook(Book book) {
        return bookRepository.save(book);
    }

    public LocalDate estimatedReturnDate() {
        return LocalDate.now().plusDays(LOAN_PERIOD_DAYS);
    }
}
