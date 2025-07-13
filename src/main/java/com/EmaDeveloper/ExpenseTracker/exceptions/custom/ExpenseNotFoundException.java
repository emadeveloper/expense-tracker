package com.EmaDeveloper.ExpenseTracker.exceptions.custom;

public class ExpenseNotFoundException extends RuntimeException{
    public ExpenseNotFoundException(Long id) {
        super("Expense not found with id: " + id);
    }
}
