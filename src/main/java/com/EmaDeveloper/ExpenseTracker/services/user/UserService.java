package com.EmaDeveloper.ExpenseTracker.services.user;

import com.EmaDeveloper.ExpenseTracker.dto.UserRegistrationRequest;
import com.EmaDeveloper.ExpenseTracker.dto.UserResponseDTO;
import jakarta.validation.Valid;

public interface UserService {

    UserResponseDTO getUserById(Long id);

    UserResponseDTO getUserByUsername(String username);

    UserResponseDTO registerUser(@Valid UserRegistrationRequest registrationRequest);

    UserResponseDTO updateUser(Long id, @Valid UserRegistrationRequest updatedUserRequest);

    void deleteUser(Long id);
}
