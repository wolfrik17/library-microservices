package com.library.lending_service.service;

import com.library.lending_service.entity.Loan;
import com.library.lending_service.exception.ResourceNotFoundException;
import com.library.lending_service.repository.LoanRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import com.library.lending_service.client.UserClient;
import feign.FeignException;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LoanService {

    private final LoanRepository loanRepository;
    private final UserClient userClient;

    @CircuitBreaker(name = "userClientBreaker", fallbackMethod = "createLoanFallback")
    public Loan createLoan(Loan loan) {
        userClient.getUserById(loan.getUserId());

        if (loan.getLoanDate() == null) {
            loan.setLoanDate(LocalDate.now());
        }
        return loanRepository.save(loan);
    }

    // Fallback logic
    public Loan createLoanFallback(Loan loan, Throwable t) {
        // Log the actual cause for debugging
        System.err.println("Circuit Breaker triggered. Reason: " + t.getMessage());

        // Return a response that the controller can interpret as a service failure
        throw new RuntimeException("User verification service is currently unreachable.");
    }

    // Read All
    public List<Loan> getAllLoans() {
        return loanRepository.findAll();
    }

    // Read One
    public Loan getLoanById(Long id) {
        return loanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Loan not found with id: " + id));
    }

    // Read by User
    public List<Loan> getLoansByUserId(Long userId) {
        return loanRepository.findByUserId(userId);
    }

    // Update (Return a book)
    public Loan returnBook(Long id) {
        Loan existingLoan = getLoanById(id);
        // Business logic: Set return date to today
        existingLoan.setReturnDate(LocalDate.now());
        return loanRepository.save(existingLoan);
    }

    // Delete
    public void deleteLoan(Long id) {
        Loan loan = getLoanById(id);
        loanRepository.delete(loan);
    }

    public Page<Loan> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
                Sort.by(sortField).descending();

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
        return loanRepository.findAll(pageable);
    }
}