package com.library.catalog_service.controller;

import com.library.catalog_service.entity.Book;
import com.library.catalog_service.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// @RestController tells Spring this file returns raw JSON data, not HTML pages
@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookRestController {

    private final BookService bookService;

    // Triggers the Cacheable getAllBooks() method
    @GetMapping
    public List<Book> getAllBooksAPI() {
        return bookService.getAllBooks();
    }

    // Triggers the Cacheable getBookById() method
    @GetMapping("/{id}")
    public Book getBookByIdAPI(@PathVariable Long id) {
        return bookService.getBookById(id);
    }
}