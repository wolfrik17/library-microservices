package com.library.lending_service.service;

import com.library.lending_service.entity.Loan;
import com.library.lending_service.exception.ResourceNotFoundException;
import com.library.lending_service.repository.LoanRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LoanServiceTest {

    @Mock
    private LoanRepository loanRepository;

    @InjectMocks
    private LoanService loanService;

    private Loan testLoan;

    @BeforeEach
    void setUp() {
        testLoan = Loan.builder()
                .id(1L)
                .userId(100L)
                .bookId(200L)
                .loanDate(LocalDate.now().minusDays(5))
                .build();
    }

    @Test
    void createLoan_ShouldSetDateAndReturnSavedLoan() {
        Loan newLoan = Loan.builder().userId(100L).bookId(200L).build(); // No date provided
        when(loanRepository.save(any(Loan.class))).thenReturn(newLoan);

        Loan savedLoan = loanService.createLoan(newLoan);

        assertNotNull(savedLoan);
        assertNotNull(savedLoan.getLoanDate()); // Business logic verify
        verify(loanRepository, times(1)).save(any(Loan.class));
    }

    @Test
    void getLoanById_WhenExists_ShouldReturnLoan() {
        when(loanRepository.findById(1L)).thenReturn(Optional.of(testLoan));

        Loan foundLoan = loanService.getLoanById(1L);

        assertNotNull(foundLoan);
        assertEquals(100L, foundLoan.getUserId());
    }

    @Test
    void getLoanById_WhenDoesNotExist_ShouldThrowException() {
        when(loanRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> loanService.getLoanById(99L));
    }

    @Test
    void getLoansByUserId_ShouldReturnUserLoans() {
        when(loanRepository.findByUserId(100L)).thenReturn(Arrays.asList(testLoan));

        List<Loan> loans = loanService.getLoansByUserId(100L);

        assertEquals(1, loans.size());
        verify(loanRepository, times(1)).findByUserId(100L);
    }

    @Test
    void returnBook_ShouldSetReturnDate() {
        when(loanRepository.findById(1L)).thenReturn(Optional.of(testLoan));
        when(loanRepository.save(any(Loan.class))).thenReturn(testLoan);

        Loan returnedLoan = loanService.returnBook(1L);

        assertNotNull(returnedLoan.getReturnDate()); // Business logic verify
        assertEquals(LocalDate.now(), returnedLoan.getReturnDate());
        verify(loanRepository, times(1)).save(testLoan);
    }

    @Test
    void deleteLoan_ShouldCallRepositoryDelete() {
        when(loanRepository.findById(1L)).thenReturn(Optional.of(testLoan));
        doNothing().when(loanRepository).delete(testLoan);

        loanService.deleteLoan(1L);

        verify(loanRepository, times(1)).delete(testLoan);
    }
}