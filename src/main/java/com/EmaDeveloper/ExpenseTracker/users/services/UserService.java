package com.EmaDeveloper.ExpenseTracker.users.services;

import com.EmaDeveloper.ExpenseTracker.users.dto.UserRegistrationRequest;
import com.EmaDeveloper.ExpenseTracker.users.dto.UserResponseDTO;
import jakarta.validation.Valid;

public interface UserService {

    UserResponseDTO getUserById(Long id);

    UserResponseDTO getUserByUsername(String username);

    UserResponseDTO registerUser(@Valid UserRegistrationRequest registrationRequest);

    UserResponseDTO updateUser(Long id, @Valid UserRegistrationRequest updatedUserRequest);

    void deleteUser(Long id);
}
