package com.EmaDeveloper.ExpenseTracker.expenses.mapper;

import com.EmaDeveloper.ExpenseTracker.expenses.dto.ExpenseResponseDTO;
import com.EmaDeveloper.ExpenseTracker.expenses.entities.Expense;
import com.EmaDeveloper.ExpenseTracker.users.dto.UserSummaryDTO;

public class ExpenseMapper {
    public static  Expense toEntity(ExpenseResponseDTO expenseResponseDTO){
        Expense expense = new Expense();
        expense.setId(expenseResponseDTO.getId());
        expense.setDate(expenseResponseDTO.getDate());
        expense.setTitle(expenseResponseDTO.getTitle());
        expense.setCategory(expenseResponseDTO.getCategory());
        expense.setAmount(expenseResponseDTO.getAmount());
        expense.setDescription(expenseResponseDTO.getDescription());

        UserSummaryDTO userSummary = expenseResponseDTO.getUser();

        return expense;
    }

    public static ExpenseResponseDTO toResponseDTO(Expense expense){
        ExpenseResponseDTO responseDTO = new ExpenseResponseDTO();
        responseDTO.setId(expense.getId());
        responseDTO.setDate(expense.getDate());
        responseDTO.setTitle(expense.getTitle());
        responseDTO.setCategory(expense.getCategory());
        responseDTO.setAmount(expense.getAmount());
        responseDTO.setDescription(expense.getDescription());

        UserSummaryDTO userSummary = new UserSummaryDTO();
        userSummary.setId(expense.getUser().getId());
        userSummary.setUsername(expense.getUser().getUsername());
        userSummary.setEmail(expense.getUser().getEmail());

        responseDTO.setUser(userSummary);
        return responseDTO;
    }
}
