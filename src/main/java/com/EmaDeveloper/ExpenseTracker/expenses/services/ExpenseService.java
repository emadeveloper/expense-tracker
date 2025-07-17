package com.EmaDeveloper.ExpenseTracker.expenses.services;

import com.EmaDeveloper.ExpenseTracker.expenses.dto.ExpenseRequestDTO;
import com.EmaDeveloper.ExpenseTracker.expenses.dto.ExpenseResponseDTO;

import java.util.List;

public interface ExpenseService {

    List<ExpenseResponseDTO> getAllExpenses();

    List<ExpenseResponseDTO> getAllExpensesByCurrentUser();

    ExpenseResponseDTO getExpenseById(Long id);

    ExpenseResponseDTO postExpense(ExpenseRequestDTO expenseDTO);

    ExpenseResponseDTO updateExpense(Long id, ExpenseRequestDTO expenseDTO);

    void deleteExpense(Long id);
}
