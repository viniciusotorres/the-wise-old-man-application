package com.example.the_wise_old_man.dto.players;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PlayerAuthDTO(
        @Email(message = "Email should be valid")
        @NotBlank(message = "Email is mandatory")
        String email,

        @NotBlank(message = "Password is mandatory")
        @Size(min = 6, message = "Password should have at least 6 characters")
        String password
) {
}