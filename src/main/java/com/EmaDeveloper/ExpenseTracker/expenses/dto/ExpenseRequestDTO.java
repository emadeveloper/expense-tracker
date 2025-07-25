package com.EmaDeveloper.ExpenseTracker.expenses.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ExpenseRequestDTO {

    @NotNull(message = "Title is required")
    @Size(min = 5, max = 50, message = "Title must be between 5 and 50 characters")
    private String title;

    @NotNull(message = "Description is required")
    @Size(min = 1, max = 200, message = "Description must be between 1 and 200 characters")
    private String description;

    @NotNull(message = "Category is required")
    private String category;

    @NotNull(message = "Date is required")
    @PastOrPresent(message = "Date must be in the past or present")
    private LocalDate date;

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be positive")
    private Double amount;
}
