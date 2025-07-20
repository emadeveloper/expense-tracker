package com.EmaDeveloper.ExpenseTracker.stats.dto;

import com.EmaDeveloper.ExpenseTracker.expenses.dto.ExpenseResponseDTO;
import com.EmaDeveloper.ExpenseTracker.expenses.entities.Expense;
import com.EmaDeveloper.ExpenseTracker.incomes.dto.IncomeResponseDTO;
import com.EmaDeveloper.ExpenseTracker.incomes.entities.Income;
import com.EmaDeveloper.ExpenseTracker.users.dto.UserSummaryDTO;
import lombok.Data;

@Data
public class StatsDTO {

    private Double income;
    private Double expense;
    private IncomeResponseDTO latestIncome;
    private ExpenseResponseDTO latestExpense;
    private Double balance;
    private Double minIncome;
    private Double maxIncome;
    private Double minExpense;
    private Double maxExpense;
    private UserSummaryDTO user;
}
