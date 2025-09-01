package com.EmaDeveloper.ExpenseTracker.stats.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExpenseStatsDTO {
    private MinMaxDTO min;
    private MinMaxDTO max;
    
}
