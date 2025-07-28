package com.EmaDeveloper.ExpenseTracker.exceptions.custom;

public class ExpenseNotFoundException extends RuntimeException{
    private final Long id;

    public ExpenseNotFoundException(Long id) {
        super("Expense not found with id: " + id);
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
