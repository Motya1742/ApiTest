package com.example.authenticationService.models;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    @NotBlank(message = "This field must be not empty!")
    private String username;
    @NotBlank(message = "This field must be not empty!")
    private String password;
}
