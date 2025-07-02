package com.EmaDeveloper.ExpenseTracker.controller;

import com.EmaDeveloper.ExpenseTracker.dto.IncomeDTO;
import com.EmaDeveloper.ExpenseTracker.entities.Income;
import com.EmaDeveloper.ExpenseTracker.services.Income.IncomeService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.util.List;

@Validated
@RequiredArgsConstructor
@RestController
@CrossOrigin(origins = "http://localhost:5173/", allowCredentials = "true")
@RequestMapping("/api/income")
public class IncomeController {
    private final IncomeService incomeService;

    @Operation(summary = "Get all incomes")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Incomes retrieved successfully"),
            @ApiResponse(responseCode = "204", description = "No incomes found"),
            @ApiResponse(responseCode = "500", description = "Error retrieving incomes")
    })

    @GetMapping("/all")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> getAllIncomes() {
        List<IncomeDTO> incomes = incomeService.getAllIncomes();

        // Check if the list is null
        if (incomes == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving incomes");
        }
        // Check if the list is empty
        if (incomes.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No incomes found");
        }
        // if not empty, return the list of incomes
        return ResponseEntity.ok(incomes);
    }

    @Operation(summary = "Get income by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Income retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Income not found"),
            @ApiResponse(responseCode = "500", description = "Error retrieving income")
    })

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> getIncomeById(@PathVariable Long id){
        try {
            Income income = incomeService.getIncomeById(id);
            return ResponseEntity.ok(income);
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Income not found with id: " + id);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong");
        }
    }

    @Operation(summary = "Create a new income")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Income created successfully"),
            @ApiResponse(responseCode = "500", description = "Error creating income")
    })

    @PostMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> postIncome(@Valid @RequestBody IncomeDTO incomeDTO) {
        Income createdIncome = incomeService.postIncome(incomeDTO);

        return createdIncome != null
                ? ResponseEntity.status(HttpStatus.CREATED).body(createdIncome)
                : ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error creating income");
    }

    @Operation(summary = "Update an existing income")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Income updated successfully"),
            @ApiResponse(responseCode = "404", description = "Income not found"),
            @ApiResponse(responseCode = "500", description = "Error updating income")
    })

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> updateIncome(@PathVariable Long id, @Valid @RequestBody IncomeDTO incomeDTO) {
        try {
            return ResponseEntity.ok(incomeService.updateIncome(id, incomeDTO));
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Income not found with id: " + id);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong");
        }
    }

    @Operation(summary = "Delete an income")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Income deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Income not found"),
            @ApiResponse(responseCode = "500", description = "Error deleting income")
    })

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteIncome(@PathVariable Long id){
        try {
            incomeService.deleteIncome(id);
            return ResponseEntity.ok("Income deleted successfully");
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Income not found with id: " + id);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong");
        }
    }
}