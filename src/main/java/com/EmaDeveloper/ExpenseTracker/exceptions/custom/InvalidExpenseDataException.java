package com.EmaDeveloper.ExpenseTracker.exceptions.custom;

public class InvalidExpenseDataException extends RuntimeException {
    public InvalidExpenseDataException (String message){
        super(message);
    }
}
