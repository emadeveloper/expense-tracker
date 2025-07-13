package com.EmaDeveloper.ExpenseTracker.exceptions.handler;

import com.EmaDeveloper.ExpenseTracker.exceptions.dto.ApiError;
import com.EmaDeveloper.ExpenseTracker.exceptions.custom.ExpenseNotFoundException;
import com.EmaDeveloper.ExpenseTracker.exceptions.custom.InvalidExpenseDataException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ExpenseNotFoundException.class)
    public ResponseEntity<ApiError> handleExpenseNotFoundException(ExpenseNotFoundException ex) {
        ApiError error = new ApiError(LocalDateTime.now(), HttpStatus.NOT_FOUND.value(), ex.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(InvalidExpenseDataException.class)
    public ResponseEntity<ApiError> handleInvalidExpenseDataException(InvalidExpenseDataException ex) {
        ApiError error = new ApiError(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(), ex.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

}
