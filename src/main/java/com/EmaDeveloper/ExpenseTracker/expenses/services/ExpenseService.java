package com.EmaDeveloper.ExpenseTracker.expenses.services;

import com.EmaDeveloper.ExpenseTracker.expenses.dto.ExpenseRequestDTO;
import com.EmaDeveloper.ExpenseTracker.expenses.dto.ExpenseResponseDTO;
import com.EmaDeveloper.ExpenseTracker.expenses.entities.Expense;

import java.util.List;

public interface ExpenseService {

    List<Expense> getAllExpenses();

    List<Expense> getAllExpensesByCurrentUser();

    Expense getExpenseById(Long id);

    ExpenseResponseDTO postExpense(ExpenseRequestDTO expenseDTO);

    Expense updateExpense(Long id, ExpenseRequestDTO expenseDTO);

    void deleteExpense(Long id);
}
