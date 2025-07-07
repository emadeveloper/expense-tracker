package com.EmaDeveloper.ExpenseTracker.stats.controller;

import com.EmaDeveloper.ExpenseTracker.stats.services.StatsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173/", allowCredentials = "true")
@RequestMapping("api/stats")
public class StatsController {

    private final StatsService statsService;

    @Operation(summary = "Get chart data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Chart data retrieved successfully"),
            @ApiResponse(responseCode = "204", description = "No chart data found"),
            @ApiResponse(responseCode = "500", description = "Error retrieving chart data")
    })

    @GetMapping("/chart")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> getChartDetails() {
        if (statsService.getChartData() == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving chart data");
        }
        if (statsService.getChartData().getExpenseList().isEmpty() && statsService.getChartData().getIncomeList().isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No chart data found");
        }
        return ResponseEntity.status(HttpStatus.OK).body(statsService.getChartData());
    }

    @Operation(summary = "Get Stats")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Stats retrieved successfully"),
            @ApiResponse(responseCode = "204", description = "No stats found"),
            @ApiResponse(responseCode = "500", description = "Error retrieving stats")
    })

    @GetMapping("/stats")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> getStats() {
        if (statsService.getStats() == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving stats");
        }
        if (statsService.getStats().getIncome() == 0 && statsService.getStats().getExpense() == 0) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No stats found");
        }
        return ResponseEntity.ok(statsService.getStats());
    }
}