package com.EmaDeveloper.ExpenseTracker.expenses.mapper;

import com.EmaDeveloper.ExpenseTracker.expenses.dto.ExpenseRequestDTO;
import com.EmaDeveloper.ExpenseTracker.expenses.dto.ExpenseResponseDTO;
import com.EmaDeveloper.ExpenseTracker.expenses.entities.Expense;
import com.EmaDeveloper.ExpenseTracker.users.dto.UserSummaryDTO;
import com.EmaDeveloper.ExpenseTracker.users.entities.User;

import java.time.LocalDate;

public class ExpenseMapper {
    public static  Expense toEntity(ExpenseRequestDTO expenseRequestDTO, User user){
        Expense expense = new Expense();
        expense.setDate(expenseRequestDTO.getDate() != null ? expenseRequestDTO.getDate() : LocalDate.now());
        expense.setTitle(expenseRequestDTO.getTitle());
        expense.setCategory(expenseRequestDTO.getCategory());
        expense.setAmount(expenseRequestDTO.getAmount());
        expense.setDescription(expenseRequestDTO.getDescription());
        expense.setIcon(expenseRequestDTO.getIcon());
        expense.setUser(user);

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
        responseDTO.setIcon(expense.getIcon());

        UserSummaryDTO userSummary = new UserSummaryDTO();
        userSummary.setId(expense.getUser().getId());
        userSummary.setUsername(expense.getUser().getUsername());
        userSummary.setEmail(expense.getUser().getEmail());

        responseDTO.setUser(userSummary);
        return responseDTO;
    }
}
