package com.EmaDeveloper.ExpenseTracker.services.user;

import com.EmaDeveloper.ExpenseTracker.dto.UserResponseDTO;
import com.EmaDeveloper.ExpenseTracker.entities.UserRegistrationRequest;

import java.util.List;

public interface UserService {

    UserResponseDTO getUserById(Long id);

    UserResponseDTO getUserByUsername(String username);

    UserResponseDTO registerUser(UserRegistrationRequest registrationRequest);

    UserResponseDTO updateUser(Long id, UserRegistrationRequest updatedUserRequest);

    void deleteUser(Long id);
}
