package com.EmaDeveloper.ExpenseTracker.incomes.dto;

import com.EmaDeveloper.ExpenseTracker.users.dto.UserSummaryDTO;
import lombok.Data;

import java.time.LocalDate;

@Data
public class IncomeResponseDTO {
    private Long id;
    private String title;
    private String description;
    private String category;
    private LocalDate date;
    private Double amount;
    private UserSummaryDTO user;
}
