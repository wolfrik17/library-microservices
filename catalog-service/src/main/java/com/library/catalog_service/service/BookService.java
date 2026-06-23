package com.library.catalog_service.service;

import com.library.catalog_service.entity.Book;
import com.library.catalog_service.exception.ResourceNotFoundException;
import com.library.catalog_service.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    // Create - Wipes the cache so the new book shows up immediately
    @CacheEvict(value = {"booksList", "book"}, allEntries = true)
    public Book createBook(Book book) {
        return bookRepository.save(book);
    }

    // Read All - Saves the entire list of books into Redis
    @Cacheable(value = "booksList")
    public List<Book> getAllBooks() {
        System.out.println("Fetching all books from the PostgreSQL database (Not in Redis yet!)...");
        return bookRepository.findAll();
    }

    // Read One - Saves individual books into Redis using their ID as the key
    @Cacheable(value = "book", key = "#id")
    public Book getBookById(Long id) {
        System.out.println("Fetching book ID " + id + " from the PostgreSQL database (Not in Redis yet!)...");
        return bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));
    }

    // Update - Wipes the cache so the updated details show up
    @CacheEvict(value = {"booksList", "book"}, allEntries = true)
    public Book updateBook(Long id, Book bookDetails) {
        Book existingBook = getBookById(id);
        existingBook.setTitle(bookDetails.getTitle());
        existingBook.setIsbn(bookDetails.getIsbn());
        return bookRepository.save(existingBook);
    }

    // Delete - Wipes the cache so deleted books disappear
    @CacheEvict(value = {"booksList", "book"}, allEntries = true)
    public void deleteBook(Long id) {
        Book book = getBookById(id);
        bookRepository.delete(book);
    }

    public Page<Book> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
                Sort.by(sortField).descending();

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
        return bookRepository.findAll(pageable);
    }
}