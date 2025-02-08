package com.example.bookTrackerService.repository;

import com.example.bookTrackerService.models.TrackingBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

@Repository
public interface TrackingBookRepository extends JpaRepository<TrackingBook, Long> {
    Optional<TrackingBook> findByBookId(Long bookId);

    @Query(value = "SELECT * FROM trackingbook " + //jpql чекнуть альтернативу как решение n+1
            "WHERE (return_by IS NULL AND borrowed_at IS NULL) " +
            "OR (return_by < NOW() AND borrowed_at IS NULL) " +
            "OR (return_by < NOW() AND borrowed_at IS NOT NULL)", nativeQuery = true)
    List<TrackingBook> findAvailableBooks();

    boolean existsByBookId(Long bookId);

    void deleteByBookId(Long bookId);
}
