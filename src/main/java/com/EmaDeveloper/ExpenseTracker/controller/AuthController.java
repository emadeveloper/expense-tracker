package com.EmaDeveloper.ExpenseTracker.controller;

import com.EmaDeveloper.ExpenseTracker.dto.AuthResponseDTO;
import com.EmaDeveloper.ExpenseTracker.dto.LoginRequestDTO;
import com.EmaDeveloper.ExpenseTracker.dto.UserRegistrationRequest;
import com.EmaDeveloper.ExpenseTracker.services.auth.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/auth")
@RestController
@CrossOrigin(origins = "http://localhost:5173/", allowCredentials = "true")
@RequiredArgsConstructor
@Validated
public class AuthController {

    private final AuthService authService;


    @Operation(summary = "Register a new user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User registered successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid registration request"),
            @ApiResponse(responseCode = "500", description = "Error registering user")
    })
    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> registerUser(@Valid @RequestBody UserRegistrationRequest request){
        return ResponseEntity.ok(authService.registerUser(request));
    }

    @Operation(summary = "Login a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User logged in successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid login request"),
            @ApiResponse(responseCode = "500", description = "Error logging in user")
    })
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> loginUser(@Valid @RequestBody LoginRequestDTO request){
        return ResponseEntity.ok(authService.loginUser(request));
    }
}
