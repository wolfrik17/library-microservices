package com.library.catalog_service.controller;

import com.library.catalog_service.entity.Book;
import com.library.catalog_service.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
    public String viewBooks(
            @RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
            @RequestParam(value = "sortField", defaultValue = "title") String sortField,
            @RequestParam(value = "sortDir", defaultValue = "asc") String sortDir,
            Model model) {

        int pageSize = 5; // Configurable page size requirement
        Page<Book> page = bookService.findPaginated(pageNo, pageSize, sortField, sortDir);

        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());

        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");

        model.addAttribute("books", page.getContent());
        model.addAttribute("newBook", new Book());
        return "books";
    }

    // @Valid triggers server-side Bean Validation
    @PostMapping
    public String addBook(@Valid @ModelAttribute("newBook") Book book, BindingResult result, Model model) {
        if (result.hasErrors()) {
            // If validation fails, reload the first page of the table so the UI renders correctly
            Page<Book> page = bookService.findPaginated(1, 5, "title", "asc");

            model.addAttribute("currentPage", 1);
            model.addAttribute("totalPages", page.getTotalPages());
            model.addAttribute("totalItems", page.getTotalElements());
            model.addAttribute("sortField", "title");
            model.addAttribute("sortDir", "asc");
            model.addAttribute("reverseSortDir", "desc");
            model.addAttribute("books", page.getContent());

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