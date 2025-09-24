package com.EmaDeveloper.ExpenseTracker.expenses.dto;

import com.EmaDeveloper.ExpenseTracker.users.dto.UserSummaryDTO;
import com.EmaDeveloper.ExpenseTracker.users.entities.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExpenseResponseDTO {
    private Long id;
    private String title;
    private String description;
    private String category;
    private LocalDate date;
    private Double amount;
    private String icon;
    private UserSummaryDTO user;

}
