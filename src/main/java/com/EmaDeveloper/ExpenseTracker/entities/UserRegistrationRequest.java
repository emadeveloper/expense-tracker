package com.EmaDeveloper.ExpenseTracker.entities;

import lombok.Data;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Data
public class UserRegistrationRequest {

    @NotNull(message = "Username cannot be null")
    @Size(min = 5, max = 50, message = "Username must be between 5 and 50 characters")
    private String username;

    @NotNull(message = "Password cannot be null")
    @Size(min = 8, max= 20, message = "Password must be between 8 and 20 characters")
    private String password;

    @Email(message = "Email should be valid")
    @NotNull(message = "Email cannot be null")
    private String email;
}
