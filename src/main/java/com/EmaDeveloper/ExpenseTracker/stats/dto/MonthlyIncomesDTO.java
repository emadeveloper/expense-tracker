package com.EmaDeveloper.ExpenseTracker.stats.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MonthlyIncomesDTO {
    private int year;
    private int month;

    private Double totalAmount;

    public String getLabel() {
        return String.format("%04d-%02d", year, month);
    }
}
