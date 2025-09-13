package com.EmaDeveloper.ExpenseTracker.expenses.controller;

import com.EmaDeveloper.ExpenseTracker.expenses.dto.ExpenseRequestDTO;
import com.EmaDeveloper.ExpenseTracker.expenses.dto.ExpenseResponseDTO;
import com.EmaDeveloper.ExpenseTracker.expenses.services.ExpenseService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173/", allowCredentials = "true")
@RequestMapping("/api/v1/expenses")
public class ExpenseController {
    private final ExpenseService expenseService;

    @Operation(summary = "Get all expenses")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Expenses retrieved successfully"),
            @ApiResponse(responseCode = "204", description = "No expenses found"),
            @ApiResponse(responseCode = "500", description = "Error retrieving expenses")
    })

    // Endpoint to get all expenses (only accessible by ADMIN)
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ExpenseResponseDTO>> getAllExpenses() {

        List<ExpenseResponseDTO> expenses = expenseService.getAllExpenses();
        return ResponseEntity.ok(expenses);
    }

    @Operation(summary = "Get all expenses by current user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Expenses retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "No expenses found for the user"),
            @ApiResponse(responseCode = "500", description = "Error retrieving expenses for the user")
    })

    // Endpoint to get all expenses by the current user
    @GetMapping("/my-expenses")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<ExpenseResponseDTO>> getAllExpensesByCurrentUser() {
        List<ExpenseResponseDTO> expenses = expenseService.getAllExpensesByCurrentUser();

        return ResponseEntity.ok(expenses);
    }

    @Operation(summary = "Get an expense by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Expense retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Expense not found"),
            @ApiResponse(responseCode = "500", description = "Error retrieving expense")
    })

    // Endpoint to get an expense by ID
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<ExpenseResponseDTO> getExpenseById(@PathVariable Long id) {
        ExpenseResponseDTO expense = expenseService.getExpenseById(id);

        return ResponseEntity.ok(expense);
    }

    @Operation(summary = "Get the last 5 expenses by current user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Expenses retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Expenses not found for the user"),
            @ApiResponse(responseCode = "500", description = "Error retrieving expenses for the user"),
    })

    @GetMapping("/my-expenses/last-5")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<ExpenseResponseDTO>> getLast5ExpensesByCurrentUser() {
        List<ExpenseResponseDTO> expenses = expenseService.getLast5ExpensesByCurrentUser();

        return ResponseEntity.ok(expenses);
    }

    @Operation(summary = "Create a new expense")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Expense created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid expense data")
    })

    // Endpoint to create a new expense
    @PostMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<ExpenseResponseDTO> postExpense(@Valid @RequestBody ExpenseRequestDTO expenseDTO) {
        ExpenseResponseDTO createdExpense = expenseService.postExpense(expenseDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdExpense);
    }

    @Operation(summary = "Update an expense by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Expense updated successfully"),
            @ApiResponse(responseCode = "404", description = "Expense not found"),
            @ApiResponse(responseCode = "400", description = "Invalid expense data")
    })

    // Endpoint to update an expense by ID
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<ExpenseResponseDTO> updateExpense(@PathVariable Long id, @RequestBody @Valid ExpenseRequestDTO expenseDTO) {
        ExpenseResponseDTO updatedExpense = expenseService.updateExpense(id, expenseDTO);
        return ResponseEntity.ok(updatedExpense);
    }

    @Operation(summary = "Delete an expense by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Expense deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Expense not found"),
            @ApiResponse(responseCode = "500", description = "Error deleting expense")
    })

    // Endpoint to delete an expense by ID
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Void> deleteExpense(@PathVariable Long id) {
        expenseService.deleteExpense(id);
        return ResponseEntity.noContent().build();
    }
}