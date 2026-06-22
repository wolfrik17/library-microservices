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

    // 1. Display the page with the form and the table
    @GetMapping
    public String viewLoansPage(Model model, @ModelAttribute("errorMessage") String errorMessage) {
        model.addAttribute("loans", loanService.getAllLoans());
        if (errorMessage != null && !errorMessage.isEmpty()) {
            model.addAttribute("errorMessage", errorMessage);
        }
        return "loans";
    }

    // 2. Accept form submissions from the user interface
    @PostMapping
    public String createLoanFromForm(@RequestParam Long userId,
                                     @RequestParam Long bookId,
                                     RedirectAttributes redirectAttributes) {
        try {
            Loan newLoan = new Loan();
            newLoan.setUserId(userId);
            newLoan.setBookId(bookId);

            // This invokes your business logic containing the OpenFeign client check
            loanService.createLoan(newLoan);

        } catch (Exception e) {
            // If OpenFeign throws an exception because user/book doesn't exist, pass the error message back safely
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to issue loan: " + e.getMessage());
        }

        // Refresh the page cleanly via redirect to show updated data or errors
        return "redirect:/loans";
    }
}