package com.example.bookTrackerService.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrackingBookWithoutIdDTO {
    private Long bookId;
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm")
    private LocalDateTime borrowedAt;
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm")
    private LocalDateTime returnBy;
}
