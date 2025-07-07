package com.EmaDeveloper.ExpenseTracker.auth.services;

import com.EmaDeveloper.ExpenseTracker.auth.dto.AuthResponseDTO;
import com.EmaDeveloper.ExpenseTracker.auth.dto.LoginRequestDTO;
import com.EmaDeveloper.ExpenseTracker.users.dto.UserRegistrationRequest;
import com.EmaDeveloper.ExpenseTracker.users.entities.User;

public interface AuthService {

    AuthResponseDTO registerUser(UserRegistrationRequest request);

    AuthResponseDTO loginUser(LoginRequestDTO request);

    User getCurrentUser();

}
