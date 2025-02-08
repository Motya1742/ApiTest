package com.example.bookTrackerService.controllers;

import com.example.bookTrackerService.models.TrackingBookWithoutIdDTO;
import com.example.bookTrackerService.services.TrackingBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tracking")
public class TrackingBookController {
    @Autowired
    private TrackingBookService trackingBookService;

    @GetMapping("/available")
    public ResponseEntity<?> getAvailableBooks() {
        return ResponseEntity.ok(trackingBookService.getAvailableBooks());
    }

    @PostMapping("/add/{id}")
    public ResponseEntity<?> createTrackingBook(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.CREATED).body(trackingBookService.saveTrackingBook(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTrackingBook(@PathVariable Long id) {
        trackingBookService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateTrackingBook(@RequestBody TrackingBookWithoutIdDTO bookDTO, @PathVariable Long id) {
        return ResponseEntity.ok(trackingBookService.updateBook(id, bookDTO));
    }
}
