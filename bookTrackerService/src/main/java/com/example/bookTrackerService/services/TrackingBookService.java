package com.example.bookTrackerService.services;

import com.example.bookTrackerService.mappers.TrackingBookMapper;
import com.example.bookTrackerService.mappers.TrackingBookWithoutIdMapper;
import com.example.bookTrackerService.models.TrackingBook;
import com.example.bookTrackerService.models.TrackingBookDTO;
import com.example.bookTrackerService.models.TrackingBookWithoutIdDTO;
import com.example.bookTrackerService.repository.TrackingBookRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TrackingBookService {
    @Autowired
    private TrackingBookRepository trackingBookRepository;
    private final TrackingBookMapper trackingBookMapper = TrackingBookMapper.INSTANCE;
    private final TrackingBookWithoutIdMapper trackingBookWithoutIdMapper = TrackingBookWithoutIdMapper.INSTANCE;

    @Transactional
    public void deleteById(Long id) throws EntityNotFoundException {
        if (!trackingBookRepository.existsByBookId(id)) {
            throw new EntityNotFoundException("Library Book with ID " + id + " not found");
        }
        trackingBookRepository.deleteByBookId(id);
    }

    public List<TrackingBookDTO> getAvailableBooks() {
        List<TrackingBook> availableBooks = trackingBookRepository.findAvailableBooks();
        return availableBooks.stream()
                .map(TrackingBookMapper.INSTANCE::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public TrackingBookDTO saveTrackingBook(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Id can't be null!");
        }
        TrackingBook trackingBook = new TrackingBook();
        trackingBook.setBookId(id);
        trackingBook.setBorrowedAt(null);
        trackingBook.setReturnBy(null);
        trackingBookRepository.save(trackingBook);
        return trackingBookMapper.toDto(trackingBook);
    }

    @Transactional
    public TrackingBookWithoutIdDTO updateBook(Long id, TrackingBookWithoutIdDTO updatedBookDTO) throws EntityNotFoundException {
        return trackingBookRepository.findByBookId(id)
                .map(book -> {
                    updateBorrowedAt(book, updatedBookDTO.getBorrowedAt());
                    updateReturnBy(book, updatedBookDTO.getReturnBy());

                    return trackingBookWithoutIdMapper.toDto(trackingBookRepository.save(book));
                })
                .orElseThrow(() -> new EntityNotFoundException("Tracking Book not found!"));
    }

    private void updateBorrowedAt(TrackingBook book, LocalDateTime borrowedAt) {
        if (borrowedAt != null) {
            if (borrowedAt.isAfter(LocalDateTime.now())) {
                throw new IllegalArgumentException("Borrowed date cannot be in the future");
            }
            book.setBorrowedAt(borrowedAt);
        }
    }

    private void updateReturnBy(TrackingBook book, LocalDateTime returnBy) {
        if (returnBy != null) {
            if (returnBy.isBefore(LocalDateTime.now())) {
                throw new IllegalArgumentException("Return date must be in the future");
            }
            if (book.getBorrowedAt() != null && returnBy.isBefore(book.getBorrowedAt())) {
                throw new IllegalArgumentException("Return date cannot be before borrowed date");
            }
            book.setReturnBy(returnBy);
        }
    }
}
