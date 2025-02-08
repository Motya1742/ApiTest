package com.example.bookStorageService;

import com.example.bookStorageService.models.Book;
import com.example.bookStorageService.models.BookDTO;
import com.example.bookStorageService.models.BookWithoutIdDTO;
import com.example.bookStorageService.repository.BookRepository;
import com.example.bookStorageService.services.BookService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

	@Mock
	private BookRepository bookRepository;

	@Mock
	private WebClient.Builder webClientBuilder;

	@InjectMocks
	private BookService bookService;

	private Book book;
	private BookDTO bookDTO;
	private BookWithoutIdDTO bookWithoutIdDTO;

	@BeforeEach
	void setUp() {
		book = new Book(1L, "Test Title", "123-4-56789-012-3", "Fiction", "Test Description", "Test Author");
		bookDTO = new BookDTO("Test Title", "123-4-56789-012-3", "Fiction", "Test Description", "Test Author");
		bookWithoutIdDTO = new BookWithoutIdDTO("Test title", "123-4-56789-012-3", "Fiction", "Test Description", "Test Author");
	}

	@Test
	void findBookById_ShouldReturnBookDTO_WhenBookExists() {
		when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
		BookWithoutIdDTO foundBook = bookService.findBookById(1L);
		assertNotNull(foundBook);
		assertEquals(book.getIsbn(), foundBook.getIsbn());
	}

	@Test
	void findBookById_ShouldThrowException_WhenBookNotFound() {
		when(bookRepository.findById(1L)).thenReturn(Optional.empty());
		assertThrows(EntityNotFoundException.class, () -> bookService.findBookById(1L));
	}

	@Test
	void findBookByIsbn_ShouldReturnBookDTO_WhenBookExists() {
		when(bookRepository.findByIsbn("123-4-56789-012-3")).thenReturn(Optional.of(book));
		BookDTO foundBook = bookService.findBookByIsbn("123-4-56789-012-3");
		assertNotNull(foundBook);
		assertEquals(book.getIsbn(), foundBook.getIsbn());
	}

	@Test
	void findBookByIsbn_ShouldThrowException_WhenBookNotFound() {
		when(bookRepository.findByIsbn("123-4-56789-012-3")).thenReturn(Optional.empty());
		assertThrows(EntityNotFoundException.class, () -> bookService.findBookByIsbn("123-4-56789-012-3"));
	}

	@Test
	void saveBook_ShouldSaveAndReturnBook_WhenValid() {
		when(bookRepository.findByIsbn(bookWithoutIdDTO.getIsbn())).thenReturn(Optional.empty());
		when(bookRepository.save(any(Book.class))).thenReturn(book);
		BookWithoutIdDTO savedBook = bookService.saveBook(bookWithoutIdDTO, "test-token");
		assertNotNull(savedBook);
		assertEquals(bookWithoutIdDTO.getIsbn(), savedBook.getIsbn());
	}

	@Test
	void saveBook_ShouldThrowException_WhenBookAlreadyExists() {
		when(bookRepository.findByIsbn(bookWithoutIdDTO.getIsbn())).thenReturn(Optional.of(book));
		assertThrows(EntityExistsException.class, () -> bookService.saveBook(bookWithoutIdDTO, "test-token"));
	}

	@Test
	void deleteById_ShouldDeleteBook_WhenBookExists() {
		when(bookRepository.existsById(1L)).thenReturn(true);
		doNothing().when(bookRepository).deleteById(1L);
		assertDoesNotThrow(() -> bookService.deleteById(1L, "test-token"));
		verify(bookRepository, times(1)).deleteById(1L);
	}

	@Test
	void deleteById_ShouldThrowException_WhenBookNotFound() {
		when(bookRepository.existsById(1L)).thenReturn(false);
		assertThrows(EntityNotFoundException.class, () -> bookService.deleteById(1L, "test-token"));
	}
}
