package com.EmaDeveloper.ExpenseTracker.stats.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MinMaxDTO {
    private Long id;
    private String title;
    private String description;
    private String category;
    private Double amount;
    private String icon;
    private LocalDate date;
}
