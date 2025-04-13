package com.EmaDeveloper.ExpenseTracker.services;

import com.EmaDeveloper.ExpenseTracker.dto.ExpenseDTO;
import com.EmaDeveloper.ExpenseTracker.entities.Expense;

import java.util.List;

public interface ExpenseService{

    List<Expense> getAllExpenses();

    Expense getExpenseById(Long id);

    Expense postExpense(ExpenseDTO expenseDTO);
}
