package com.EmaDeveloper.ExpenseTracker.stats.dto;

import com.EmaDeveloper.ExpenseTracker.expenses.dto.ExpenseResponseDTO;
import com.EmaDeveloper.ExpenseTracker.expenses.entities.Expense;
import com.EmaDeveloper.ExpenseTracker.incomes.dto.IncomeResponseDTO;
import com.EmaDeveloper.ExpenseTracker.incomes.entities.Income;
import com.EmaDeveloper.ExpenseTracker.users.dto.UserSummaryDTO;
import lombok.Data;

import java.util.List;

@Data
public class GraphDTO {

    private List<ExpenseResponseDTO> expenseList;

    private List<IncomeResponseDTO> incomeList;

    private UserSummaryDTO user;
}
