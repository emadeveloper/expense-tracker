package com.EmaDeveloper.ExpenseTracker.exceptions.custom;

public class IncomeNotFoundException extends  RuntimeException{
    public IncomeNotFoundException(Long id) {
        super("Income not found with id: " + id);
    }
}
