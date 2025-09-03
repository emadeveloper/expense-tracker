package com.EmaDeveloper.ExpenseTracker.stats.dto;

import com.EmaDeveloper.ExpenseTracker.expenses.dto.ExpenseResponseDTO;
import com.EmaDeveloper.ExpenseTracker.incomes.dto.IncomeResponseDTO;
import com.EmaDeveloper.ExpenseTracker.users.dto.UserSummaryDTO;
import lombok.Data;

import java.util.List;

@Data
public class StatsDTO {
    private Double balance;
    private Double totalIncomes;
    private Double totalExpenses;

    private ExpenseResponseDTO latestExpense;
    private IncomeResponseDTO latestIncome;

    private IncomeStatsDTO incomes;
    private ExpenseStatsDTO expenses;

    private List<MonthlyExpensesDTO> monthlyExpenses;

    private UserSummaryDTO user;
}
