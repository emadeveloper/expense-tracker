package com.EmaDeveloper.ExpenseTracker.controller;

import com.EmaDeveloper.ExpenseTracker.dto.IncomeDTO;
import com.EmaDeveloper.ExpenseTracker.entities.Income;
import com.EmaDeveloper.ExpenseTracker.services.Income.IncomeService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.util.List;

@Validated
@RequiredArgsConstructor
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/income")
public class IncomeController {
    private final IncomeService incomeService;

    @Operation(summary = "Create a new income")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Income created successfully"),
            @ApiResponse(responseCode = "500", description = "Error creating income")
    })

    @PostMapping
    public ResponseEntity<?> postIncome(@Valid @RequestBody IncomeDTO incomeDTO) {
        Income createdIncome = incomeService.postIncome(incomeDTO);

        return createdIncome != null
                ? ResponseEntity.status(HttpStatus.CREATED).body(createdIncome)
                : ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error creating income");
    }

    @Operation(summary = "Get all incomes")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Incomes retrieved successfully"),
            @ApiResponse(responseCode = "204", description = "No incomes found"),
            @ApiResponse(responseCode = "500", description = "Error retrieving incomes")
    })

    @GetMapping("/all")
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
}
