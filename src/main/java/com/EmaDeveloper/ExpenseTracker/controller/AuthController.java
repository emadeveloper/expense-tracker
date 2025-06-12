package com.EmaDeveloper.ExpenseTracker.controller;

import com.EmaDeveloper.ExpenseTracker.dto.AuthResponseDTO;
import com.EmaDeveloper.ExpenseTracker.dto.LoginRequestDTO;
import com.EmaDeveloper.ExpenseTracker.dto.UserRegistrationRequest;
import com.EmaDeveloper.ExpenseTracker.dto.UserResponseDTO;
import com.EmaDeveloper.ExpenseTracker.services.auth.AuthService;
import com.EmaDeveloper.ExpenseTracker.services.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
    private final UserService userService;


    @Operation(summary = "Register a new user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User registered successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
    })
    // endpoint to register a new user
    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> registerUser(
            @Parameter(description = "User registration request", required = true)
            @Valid @RequestBody UserRegistrationRequest registrationRequest) {
        UserResponseDTO user = userService.registerUser(registrationRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
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
