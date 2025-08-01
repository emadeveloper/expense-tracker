package com.EmaDeveloper.ExpenseTracker.auth.dto;

import com.EmaDeveloper.ExpenseTracker.users.dto.UserResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponseDTO {
    private String accessToken;
    private String tokenType = "Bearer";
    private UserResponseDTO userResponseDTO;

    public AuthResponseDTO(String accessToken){
        this.accessToken = accessToken;
    }
}

