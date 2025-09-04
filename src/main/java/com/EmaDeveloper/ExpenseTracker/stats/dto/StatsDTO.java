package com.EmaDeveloper.ExpenseTracker.stats.dto;

import com.EmaDeveloper.ExpenseTracker.expenses.dto.ExpenseResponseDTO;
import com.EmaDeveloper.ExpenseTracker.incomes.dto.IncomeResponseDTO;
import com.EmaDeveloper.ExpenseTracker.users.dto.UserSummaryDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatsDTO {
    private Double balance;
    private Double totalIncomes;
    private Double totalExpenses;

    private ExpenseResponseDTO latestExpense;
    private IncomeResponseDTO latestIncome;

    private IncomeStatsDTO incomes;
    private ExpenseStatsDTO expenses;

    private List<MonthlyExpensesDTO> monthlyExpenses;
    private List<MonthlyIncomesDTO> monthlyIncomes;

    private UserSummaryDTO user;
}
