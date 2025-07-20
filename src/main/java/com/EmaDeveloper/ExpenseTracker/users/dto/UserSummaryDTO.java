package com.EmaDeveloper.ExpenseTracker.users.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserSummaryDTO {
    private Long id;
    private String username;
    private String email;

    public UserSummaryDTO(Long id, String username) {
        this.id = id;
        this.username = username;
    }
}
