package com.EmaDeveloper.ExpenseTracker.controller;

import com.EmaDeveloper.ExpenseTracker.dto.ExpenseDTO;
import com.EmaDeveloper.ExpenseTracker.entities.Expense;
import com.EmaDeveloper.ExpenseTracker.repository.ExpenseRepository;
import com.EmaDeveloper.ExpenseTracker.services.ExpenseService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/expense")
public class ExpenseController {
    private final ExpenseService expenseService;
    private final ExpenseRepository expenseRepository;

    @Operation(summary = "Get all expenses")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Expenses retrieved successfully"),
            @ApiResponse(responseCode = "204", description = "No expenses found"),
            @ApiResponse(responseCode = "500", description = "Error retrieving expenses")
    })

    // Endpoint to get all expenses
    @GetMapping("/all")
    public ResponseEntity<?> getAllExpenses() {
        List<Expense> expenses = expenseService.getAllExpenses();
        // Check if the list is null
        if (expenses == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving expenses");
        }
        // Check if the list is empty
        if (expenses.isEmpty()) {
            return ResponseEntity.status((HttpStatus.NO_CONTENT)).body("No expenses found");
        }
        // If not empty, return the list of expenses
        return ResponseEntity.ok(expenses);
    }

    @Operation(summary = "Create a new expense")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Expense created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid expense data")
    })

    // Endpoint to create a new expense
    @PostMapping
    public ResponseEntity<?> postExpense(@RequestBody ExpenseDTO expenseDTO) {
        Expense createdExpense = expenseService.postExpense(expenseDTO);

        if(createdExpense != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(createdExpense);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to create expense");
        }
    }
}
