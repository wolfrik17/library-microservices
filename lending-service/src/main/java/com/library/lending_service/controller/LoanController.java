package com.library.lending_service.controller;

import com.library.lending_service.entity.Loan;
import com.library.lending_service.service.LoanService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/loans")
public class LoanController {

    private final LoanService loanService;

    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    @GetMapping
    public String viewLoansPage(Model model) {
        model.addAttribute("loans", loanService.getAllLoans());
        return "loans";
    }

    @PostMapping
    public String createLoanFromForm(@RequestParam Long userId,
                                     @RequestParam Long bookId,
                                     RedirectAttributes redirectAttributes) {
        try {
            Loan newLoan = new Loan();
            newLoan.setUserId(userId);
            newLoan.setBookId(bookId);
            loanService.createLoan(newLoan);

            redirectAttributes.addFlashAttribute("successMessage", "Loan issued successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to issue loan: " + e.getMessage());
        }
        return "redirect:/loans";
    }

    @PostMapping("/{id}/return")
    public String returnLoan(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            loanService.deleteLoan(id);
            redirectAttributes.addFlashAttribute("successMessage", "Book successfully returned!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to return book: " + e.getMessage());
        }
        return "redirect:/loans";
    }
}