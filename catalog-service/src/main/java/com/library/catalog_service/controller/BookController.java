package com.library.catalog_service.controller;

import com.library.catalog_service.entity.Book;
import com.library.catalog_service.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping
    public String viewBooks(Model model) {
        model.addAttribute("books", bookService.getAllBooks());
        model.addAttribute("newBook", new Book());
        return "books";
    }

    // @Valid triggers server-side Bean Validation
    @PostMapping
    public String addBook(@Valid @ModelAttribute("newBook") Book book, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("books", bookService.getAllBooks());
            return "books"; // Return to form with error messages
        }
        bookService.createBook(book);
        return "redirect:/books";
    }

    @GetMapping("/delete/{id}")
    public String deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return "redirect:/books";
    }
}