package com.EmaDeveloper.ExpenseTracker.incomes.dto;

import com.EmaDeveloper.ExpenseTracker.users.dto.UserSummaryDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IncomeResponseDTO {
    private Long id;
    private String title;
    private String description;
    private String category;
    private LocalDate date;
    private Double amount;
    private UserSummaryDTO user;
}
