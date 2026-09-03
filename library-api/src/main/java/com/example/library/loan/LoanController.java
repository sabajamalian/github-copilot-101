package com.example.library.loan;

import com.example.library.book.Book;
import com.example.library.book.BookRepository;
import java.time.LocalDate;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/loans")
public class LoanController {

    private static final int LOAN_PERIOD_DAYS = 14;

    private final LoanRepository loanRepository;
    private final BookRepository bookRepository;

    public LoanController(LoanRepository loanRepository, BookRepository bookRepository) {
        this.loanRepository = loanRepository;
        this.bookRepository = bookRepository;
    }

    @GetMapping
    public List<Loan> retrieveLoans() {
        return loanRepository.findAll();
    }

    @PostMapping
    public Loan checkoutLoan(@RequestBody Loan loanRequest) {
        Book book = bookRepository.findById(loanRequest.getBookId())
                .orElseThrow(() -> new IllegalArgumentException("Book not found"));

        if (!book.isAvailable()) {
            throw new IllegalStateException("Book is already checked out");
        }

        book.setAvailable(false);
        bookRepository.save(book);

        LocalDate borrowedOn = LocalDate.now();
        Loan loan = new Loan();
        loan.setBookId(loanRequest.getBookId());
        loan.setMemberId(loanRequest.getMemberId());
        loan.setBorrowedOn(borrowedOn);
        loan.setDueOn(borrowedOn.plusDays(LOAN_PERIOD_DAYS));
        loan.setReturnedOn(null);

        return loanRepository.save(loan);
    }

    @PostMapping("/{id}/return")
    public Loan returnLoan(@PathVariable Long id) {
        Loan loan = loanRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Loan not found"));

        loan.setReturnedOn(LocalDate.now());

        Book book = bookRepository.findById(loan.getBookId()).orElse(null);
        if (book != null) {
            book.setAvailable(true);
            bookRepository.save(book);
        }

        return loanRepository.save(loan);
    }
}
