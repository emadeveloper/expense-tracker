package com.EmaDeveloper.ExpenseTracker.services.auth;

import com.EmaDeveloper.ExpenseTracker.dto.AuthResponseDTO;
import com.EmaDeveloper.ExpenseTracker.dto.LoginRequestDTO;
import com.EmaDeveloper.ExpenseTracker.dto.UserRegistrationRequest;

public interface AuthService {

    AuthResponseDTO registerUser(UserRegistrationRequest request);

    AuthResponseDTO loginUser(LoginRequestDTO request);

}
