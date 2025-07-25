package com.EmaDeveloper.ExpenseTracker.exceptions.handler;

import ch.qos.logback.core.model.processor.ModelHandlerException;
import com.EmaDeveloper.ExpenseTracker.exceptions.dto.ApiError;
import com.EmaDeveloper.ExpenseTracker.exceptions.custom.ExpenseNotFoundException;
import com.EmaDeveloper.ExpenseTracker.exceptions.custom.InvalidExpenseDataException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));

        return ResponseEntity.badRequest().body(errors);
    }

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
