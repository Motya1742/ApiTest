package com.example.bookTrackerService;

import com.example.bookTrackerService.models.TrackingBook;
import com.example.bookTrackerService.models.TrackingBookDTO;
import com.example.bookTrackerService.models.TrackingBookWithoutIdDTO;
import com.example.bookTrackerService.repository.TrackingBookRepository;
import com.example.bookTrackerService.services.TrackingBookService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TrackingBookServiceTests {
	@Mock
	private TrackingBookRepository trackingBookRepository;

	@InjectMocks
	private TrackingBookService trackingBookService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testDeleteById_WhenBookExists_ShouldDelete() {
		Long bookId = 1L;
		when(trackingBookRepository.existsByBookId(bookId)).thenReturn(true);

		trackingBookService.deleteById(bookId);

		verify(trackingBookRepository, times(1)).deleteByBookId(bookId);
	}

	@Test
	void testDeleteById_WhenBookDoesNotExist_ShouldThrowException() {
		Long bookId = 1L;
		when(trackingBookRepository.existsByBookId(bookId)).thenReturn(false);

		assertThrows(EntityNotFoundException.class, () -> trackingBookService.deleteById(bookId));
	}

	@Test
	void testGetAvailableBooks_ShouldReturnList() {
		TrackingBook book = new TrackingBook(1L, 1L, null, null);
		when(trackingBookRepository.findAvailableBooks()).thenReturn(List.of(book));

		List<TrackingBookDTO> result = trackingBookService.getAvailableBooks();

		assertNotNull(result);
		assertEquals(1, result.size());
	}

	@Test
	void testSaveTrackingBook_ShouldReturnSavedBook() {
		Long bookId = 1L;
		TrackingBook trackingBook = new TrackingBook(null, bookId, null, null);
		when(trackingBookRepository.save(any(TrackingBook.class))).thenReturn(trackingBook);

		TrackingBookDTO result = trackingBookService.saveTrackingBook(bookId);

		assertNotNull(result);
		assertEquals(bookId, result.getBookId());
	}

	@Test
	void testUpdateBook_ShouldUpdateFields() {
		Long bookId = 1L;
		LocalDateTime borrowedAt = LocalDateTime.now().minusDays(1);
		LocalDateTime returnBy = LocalDateTime.now().plusDays(5);

		TrackingBook existingBook = new TrackingBook(1L, bookId, null, null);
		TrackingBookWithoutIdDTO updatedDto = new TrackingBookWithoutIdDTO(bookId, borrowedAt, returnBy);

		when(trackingBookRepository.findByBookId(bookId)).thenReturn(Optional.of(existingBook));
		when(trackingBookRepository.save(any(TrackingBook.class))).thenReturn(existingBook);

		TrackingBookWithoutIdDTO result = trackingBookService.updateBook(bookId, updatedDto);

		assertNotNull(result);
		assertEquals(borrowedAt, result.getBorrowedAt());
		assertEquals(returnBy, result.getReturnBy());
	}

	@Test
	void testUpdateBook_WhenBookNotFound_ShouldThrowException() {
		Long bookId = 1L;
		TrackingBookWithoutIdDTO updatedDto = new TrackingBookWithoutIdDTO(bookId, LocalDateTime.now(), LocalDateTime.now().plusDays(5));

		when(trackingBookRepository.findByBookId(bookId)).thenReturn(Optional.empty());

		assertThrows(EntityNotFoundException.class, () -> trackingBookService.updateBook(bookId, updatedDto));
	}
}
