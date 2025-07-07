package com.EmaDeveloper.ExpenseTracker.expenses.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ExpenseResponseDTO {
    private Long id;
    private String title;
    private String description;
    private String category;
    private LocalDate date;
    private Double amount;
}
