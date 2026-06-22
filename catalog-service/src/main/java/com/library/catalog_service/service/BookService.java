package com.library.catalog_service.service;

import com.library.catalog_service.entity.Book;
import com.library.catalog_service.exception.ResourceNotFoundException;
import com.library.catalog_service.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    // Create
    public Book createBook(Book book) {
        return bookRepository.save(book);
    }

    // Read All
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    // Read One
    public Book getBookById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));
    }

    // Update
    public Book updateBook(Long id, Book bookDetails) {
        Book existingBook = getBookById(id);
        existingBook.setTitle(bookDetails.getTitle());
        existingBook.setIsbn(bookDetails.getIsbn()); // Fixed typo here!
        return bookRepository.save(existingBook);
    }

    // Delete
    public void deleteBook(Long id) {
        Book book = getBookById(id);
        bookRepository.delete(book);
    }
}