package com.EmaDeveloper.ExpenseTracker.exceptions.custom;

public class IncomeNotFoundException extends RuntimeException {
    private final Long id;

    public IncomeNotFoundException(Long id) {
        super("Income not found with id: " + id);
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}