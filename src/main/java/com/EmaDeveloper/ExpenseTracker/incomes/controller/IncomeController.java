package com.EmaDeveloper.ExpenseTracker.incomes.controller;

import com.EmaDeveloper.ExpenseTracker.incomes.dto.IncomeRequestDTO;
import com.EmaDeveloper.ExpenseTracker.incomes.dto.IncomeResponseDTO;
import com.EmaDeveloper.ExpenseTracker.incomes.entities.Income;
import com.EmaDeveloper.ExpenseTracker.incomes.services.IncomeService;

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
@RequestMapping("/api/v1/incomes")
public class IncomeController {
    private final IncomeService incomeService;

    @Operation(summary = "Get all incomes of all users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Incomes retrieved successfully"),
            @ApiResponse(responseCode = "204", description = "No incomes found"),
            @ApiResponse(responseCode = "500", description = "Error retrieving incomes")
    })

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<IncomeResponseDTO>> getAllIncomes() {
        List<IncomeResponseDTO> incomes = incomeService.getAllIncomes();
        if (incomes.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(incomes);
    }

    @Operation(summary = "Get all incomes by current user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Incomes retrieved successfully"),
            @ApiResponse(responseCode = "204", description = "No incomes found for the user"),
            @ApiResponse(responseCode = "500", description = "Error retrieving incomes for the user")
    })
    @GetMapping("/my-incomes")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<IncomeResponseDTO>> getAllIncomesByCurrentUser() {
        List<IncomeResponseDTO> incomes = incomeService.getAllIncomesByCurrentUser();
        if (incomes.isEmpty()){
            return ResponseEntity.noContent().build();
        }
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
    public ResponseEntity<IncomeResponseDTO> getIncomeById(@PathVariable Long id){
        IncomeResponseDTO income = incomeService.getIncomeById(id);

        return ResponseEntity.ok(income);
    }

    @Operation(summary = "Get last 5 incomes by current user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Incomes retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "No incomes found for the current user"),
            @ApiResponse(responseCode = "500", description = "Error retrieving incomes for the current user"),
    })

    @GetMapping("/my-incomes/last-5")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<IncomeResponseDTO>> getLast5IncomesByCurrentUser(){
        List<IncomeResponseDTO> incomes = incomeService.getLast5IncomesByCurrentUser();
        if (incomes.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok().body(incomes);
    }

    @Operation(summary = "Create a new income")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Income created successfully"),
            @ApiResponse(responseCode = "500", description = "Error creating income")
    })

    @PostMapping("/add-income")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<IncomeResponseDTO> postIncome(@Valid @RequestBody IncomeRequestDTO incomeDTO) {
        IncomeResponseDTO createdIncome = incomeService.postIncome(incomeDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdIncome);
    }

    @Operation(summary = "Update an existing income")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Income updated successfully"),
            @ApiResponse(responseCode = "404", description = "Income not found"),
            @ApiResponse(responseCode = "500", description = "Error updating income")
    })

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<IncomeResponseDTO> updateIncome(@PathVariable Long id, @Valid @RequestBody IncomeRequestDTO incomeDTO) {
        IncomeResponseDTO updatedIncome = incomeService.updateIncome(id, incomeDTO);

        return ResponseEntity.ok(updatedIncome);
    }

    @Operation(summary = "Delete an income")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Income deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Income not found"),
            @ApiResponse(responseCode = "500", description = "Error deleting income")
    })

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteIncome(@PathVariable Long id){
        incomeService.deleteIncome(id);
        return ResponseEntity.noContent().build();
    }
}