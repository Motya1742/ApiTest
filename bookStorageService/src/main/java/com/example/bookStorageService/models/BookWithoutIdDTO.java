package com.example.bookStorageService.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookWithoutIdDTO {
    @NotBlank(message = "This field must be not empty!")
    private String title;
    @Pattern(
            regexp = "^[0-9]{3}-[0-9]-[0-9]{5}-[0-9]{3}-[0-9]$",
            message = "ISBN must be filled in the format XXX-X-XXXXX-XXX-X"
    )
    private String isbn;
    @NotBlank(message = "This field must be not empty!")
    private String genre;
    @NotBlank(message = "This field must be not empty!")
    private String description;
    @NotBlank(message = "This field must be not empty!")
    private String author;
}
