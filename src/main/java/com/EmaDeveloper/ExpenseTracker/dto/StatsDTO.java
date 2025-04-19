package com.EmaDeveloper.ExpenseTracker.dto;

import com.EmaDeveloper.ExpenseTracker.entities.Expense;
import com.EmaDeveloper.ExpenseTracker.entities.Income;
import lombok.Data;

@Data
public class StatsDTO {

    private Double income;

    private Double expense;

    private Income latestIncome;

    private Expense latestExpense;

    private Double balance;

    private Double minIncome;

    private Double maxIncome;

    private Double minExpense;

    private Double maxExpense;
}
