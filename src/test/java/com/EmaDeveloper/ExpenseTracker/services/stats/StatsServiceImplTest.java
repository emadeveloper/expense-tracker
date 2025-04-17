package com.EmaDeveloper.ExpenseTracker.services.stats;

import com.EmaDeveloper.ExpenseTracker.entities.Expense;
import com.EmaDeveloper.ExpenseTracker.entities.Income;
import com.EmaDeveloper.ExpenseTracker.repository.ExpenseRepository;
import com.EmaDeveloper.ExpenseTracker.repository.IncomeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class StatsServiceImplTest {

    @Mock
    private IncomeRepository incomeRepository;

    @Mock
    private ExpenseRepository expenseRepository;

    @InjectMocks
    private StatsServiceImpl statsServiceImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);}

    @Test
    void testGetChartData_withAnyDates_shouldReturnCorrectChartDetails() {
        // Arrange
        Income income1 = Income.builder()
                .id(1L)
                .title("Salario")
                .amount(5000.0)
                .category("Trabajo")
                .date(LocalDate.of(2025, 4, 15))
                .description("Pago mensual del salario")
                .build();

        Expense expense1 = Expense.builder()
                .id(1L)
                .title("Alquiler")
                .amount(750.0)
                .category("Gastos fijos")
                .date(LocalDate.of(2025, 4, 15))
                .description("Pago mensual del alquiler")
                .build();

        when(incomeRepository.findByDateBetween(any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(List.of(income1));
        when(expenseRepository.findByDateBetween(any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(List.of(expense1));

        // Act
        var result = statsServiceImpl.getChartData();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getIncomeList().size());
        assertEquals(1, result.getExpenseList().size());
        assertEquals("Trabajo", result.getIncomeList().get(0).getCategory());
        assertEquals("Gastos fijos", result.getExpenseList().get(0).getCategory());

        verify(incomeRepository).findByDateBetween(any(LocalDate.class), any(LocalDate.class));
        verify(expenseRepository).findByDateBetween(any(LocalDate.class), any(LocalDate.class));
    }
}

