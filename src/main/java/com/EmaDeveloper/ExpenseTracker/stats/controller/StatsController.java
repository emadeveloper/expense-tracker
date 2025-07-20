package com.EmaDeveloper.ExpenseTracker.stats.controller;

import com.EmaDeveloper.ExpenseTracker.stats.dto.GraphDTO;
import com.EmaDeveloper.ExpenseTracker.stats.dto.StatsDTO;
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
@RequestMapping("api/v1/stats")
public class StatsController {

    private final StatsService statsService;

    @Operation(summary = "Get chart data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Chart data retrieved successfully"),
            @ApiResponse(responseCode = "204", description = "No chart data found"),
            @ApiResponse(responseCode = "500", description = "Error retrieving chart data")
    })

    @GetMapping("/chart-data")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<GraphDTO> getChartDetails() {
        GraphDTO chartData = statsService.getChartData();
        if (chartData == null || chartData.getExpenseList().isEmpty() && chartData.getIncomeList().isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(chartData);
    }

    @Operation(summary = "Get Stats")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Stats retrieved successfully"),
            @ApiResponse(responseCode = "204", description = "No stats found"),
            @ApiResponse(responseCode = "500", description = "Error retrieving stats")
    })

    @GetMapping("/summary")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<StatsDTO> getStats() {
        StatsDTO stats = statsService.getStats();
        if (stats == null){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(stats);
    }
}