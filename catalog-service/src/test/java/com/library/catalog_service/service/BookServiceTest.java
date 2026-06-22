package com.library.catalog_service.service;

import com.library.catalog_service.entity.Book;
import com.library.catalog_service.exception.ResourceNotFoundException;
import com.library.catalog_service.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    private Book testBook;

    @BeforeEach
    void setUp() {
        testBook = Book.builder()
                .id(1L)
                .title("Spring Boot Microservices")
                .isbn("978-1234567890")
                .build();
    }

    @Test
    void createBook_ShouldReturnSavedBook() {
        when(bookRepository.save(any(Book.class))).thenReturn(testBook);

        Book savedBook = bookService.createBook(testBook);

        assertNotNull(savedBook);
        assertEquals("Spring Boot Microservices", savedBook.getTitle());
        verify(bookRepository, times(1)).save(any(Book.class));
    }

    @Test
    void getAllBooks_ShouldReturnListOfBooks() {
        when(bookRepository.findAll()).thenReturn(Arrays.asList(testBook, new Book()));

        List<Book> books = bookService.getAllBooks();

        assertEquals(2, books.size());
        verify(bookRepository, times(1)).findAll();
    }

    @Test
    void getBookById_WhenExists_ShouldReturnBook() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(testBook));

        Book foundBook = bookService.getBookById(1L);

        assertNotNull(foundBook);
        assertEquals("978-1234567890", foundBook.getIsbn());
    }

    @Test
    void getBookById_WhenDoesNotExist_ShouldThrowException() {
        when(bookRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> bookService.getBookById(99L));
    }

    @Test
    void updateBook_ShouldReturnUpdatedBook() {
        Book updateDetails = Book.builder().title("Updated Title").isbn("111-111").build();
        when(bookRepository.findById(1L)).thenReturn(Optional.of(testBook));
        when(bookRepository.save(any(Book.class))).thenReturn(testBook); // testBook will be modified by the service

        Book updatedBook = bookService.updateBook(1L, updateDetails);

        assertNotNull(updatedBook);
        assertEquals("Updated Title", testBook.getTitle()); // Verify the object was mutated before save
        verify(bookRepository, times(1)).save(testBook);
    }

    @Test
    void deleteBook_ShouldCallRepositoryDelete() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(testBook));
        doNothing().when(bookRepository).delete(testBook);

        bookService.deleteBook(1L);

        verify(bookRepository, times(1)).delete(testBook);
    }
}