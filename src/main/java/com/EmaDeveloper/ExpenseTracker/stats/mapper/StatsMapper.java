package com.EmaDeveloper.ExpenseTracker.stats.mapper;

import com.EmaDeveloper.ExpenseTracker.expenses.dto.ExpenseResponseDTO;
import com.EmaDeveloper.ExpenseTracker.expenses.entities.Expense;
import com.EmaDeveloper.ExpenseTracker.incomes.dto.IncomeResponseDTO;
import com.EmaDeveloper.ExpenseTracker.incomes.entities.Income;
import com.EmaDeveloper.ExpenseTracker.users.dto.UserSummaryDTO;
import com.EmaDeveloper.ExpenseTracker.users.entities.User;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StatsMapper {
    public static List<IncomeResponseDTO> mapToIncomeDTOList(List<Income> incomes){
        return incomes.stream()
                .map(income -> new IncomeResponseDTO (
                        income.getId(),
                        income.getTitle(),
                        income.getDescription(),
                        income.getCategory(),
                        income.getDate(),
                        income.getAmount(),
                        mapToUserSummaryDTO(income.getUser())
                ))
                .toList();
    }

    public static List<ExpenseResponseDTO> mapToExpenseDTOList(List<Expense> expenses){
        return expenses.stream()
                .map(expense -> new ExpenseResponseDTO(
                        expense.getId(),
                        expense.getTitle(),
                        expense.getDescription(),
                        expense.getCategory(),
                        expense.getDate(),
                        expense.getAmount(),
                        mapToUserSummaryDTO(expense.getUser())
                ))
                .toList();
    }

    private static UserSummaryDTO mapToUserSummaryDTO(User user){
        return new UserSummaryDTO(user.getId(), user.getUsername(), user.getEmail());
    }
}
