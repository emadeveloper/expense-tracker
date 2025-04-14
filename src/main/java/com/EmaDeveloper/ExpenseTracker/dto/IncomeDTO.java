package com.EmaDeveloper.ExpenseTracker.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.time.LocalDate;

@Data
public class IncomeDTO {

    private Long id;

    private  String title;

    private String description;

    private String category;

    @NotNull
    private LocalDate date;

    @NotNull
    @Positive
    private Double amount;

}
