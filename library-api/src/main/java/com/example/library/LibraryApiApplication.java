package com.example.library;

import com.example.library.book.Book;
import com.example.library.book.BookRepository;
import com.example.library.loan.Loan;
import com.example.library.loan.LoanRepository;
import com.example.library.member.Member;
import com.example.library.member.MemberRepository;
import java.time.LocalDate;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class LibraryApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(LibraryApiApplication.class, args);
    }

    @Bean
    CommandLineRunner seedLibrary(BookRepository bookRepository, MemberRepository memberRepository, LoanRepository loanRepository) {
        return args -> {
            Book cleanCode = bookRepository.save(new Book(null, "Clean Code", "Robert C. Martin", "9780132350884", true));
            Book pragmaticProgrammer = bookRepository.save(new Book(null, "The Pragmatic Programmer", "Andrew Hunt and David Thomas", "9780201616224", true));
            Book effectiveJava = bookRepository.save(new Book(null, "Effective Java", "Joshua Bloch", "9780134685991", false));
            Book domainDrivenDesign = bookRepository.save(new Book(null, "Domain-Driven Design", "Eric Evans", "9780321125217", false));
            bookRepository.save(new Book(null, "Refactoring", "Martin Fowler", "9780134757599", true));
            bookRepository.save(new Book(null, "Designing Data-Intensive Applications", "Martin Kleppmann", "9781449373320", true));
            bookRepository.save(new Book(null, "Spring in Action", "Craig Walls", "9781617297571", true));
            bookRepository.save(new Book(null, "Head First Design Patterns", "Eric Freeman and Elisabeth Robson", "9781492078005", false));

            Member ada = memberRepository.save(new Member(null, "Ada Lovelace", "ada@example.com"));
            Member grace = memberRepository.save(new Member(null, "Grace Hopper", "grace@example.com"));
            Member alan = memberRepository.save(new Member(null, "Alan Turing", "alan@example.com"));
            memberRepository.save(new Member(null, "Katherine Johnson", "katherine@example.com"));

            loanRepository.save(new Loan(null, pragmaticProgrammer.getId(), ada.getId(), LocalDate.now().minusDays(30), LocalDate.now().minusDays(16), LocalDate.now().minusDays(12)));
            loanRepository.save(new Loan(null, effectiveJava.getId(), grace.getId(), LocalDate.now().minusDays(21), LocalDate.now().minusDays(7), null));
            loanRepository.save(new Loan(null, domainDrivenDesign.getId(), alan.getId(), LocalDate.now().minusDays(4), LocalDate.now().plusDays(10), null));

            cleanCode.setAvailable(true);
            bookRepository.save(cleanCode);
        };
    }
}
