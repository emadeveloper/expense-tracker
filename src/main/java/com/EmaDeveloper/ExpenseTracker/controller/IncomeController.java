package com.EmaDeveloper.ExpenseTracker.controller;

import com.EmaDeveloper.ExpenseTracker.dto.IncomeDTO;
import com.EmaDeveloper.ExpenseTracker.entities.Income;
import com.EmaDeveloper.ExpenseTracker.repository.IncomeRepository;
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
}
