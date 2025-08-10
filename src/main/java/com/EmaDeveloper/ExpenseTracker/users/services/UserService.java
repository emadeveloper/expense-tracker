package com.EmaDeveloper.ExpenseTracker.users.services;

import com.EmaDeveloper.ExpenseTracker.auth.dto.UserRegistrationRequest;
import com.EmaDeveloper.ExpenseTracker.auth.dto.UserResponseDTO;
import jakarta.validation.Valid;

public interface UserService {

    UserResponseDTO getUserById(Long id);

    UserResponseDTO getUserByUsername(String username);

    UserResponseDTO updateUser(Long id, @Valid UserRegistrationRequest updatedUserRequest);

    void deleteUser(Long id);
}
