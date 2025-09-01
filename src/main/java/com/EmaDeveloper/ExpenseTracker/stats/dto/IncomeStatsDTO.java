package com.EmaDeveloper.ExpenseTracker.stats.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IncomeStatsDTO {
    private MinMaxDTO min;
    private MinMaxDTO max;
}
