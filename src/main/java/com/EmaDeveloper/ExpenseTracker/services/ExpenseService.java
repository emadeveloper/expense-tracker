package com.EmaDeveloper.ExpenseTracker.services;

import com.EmaDeveloper.ExpenseTracker.dto.ExpenseDTO;
import com.EmaDeveloper.ExpenseTracker.entities.Expense;

public interface ExpenseService{

    Expense getAllExpenses();

    Expense postExpense(ExpenseDTO expenseDTO);


}
