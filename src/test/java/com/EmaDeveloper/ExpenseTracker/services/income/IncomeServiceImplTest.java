package com.EmaDeveloper.ExpenseTracker.services.income;

import com.EmaDeveloper.ExpenseTracker.dto.IncomeDTO;
import com.EmaDeveloper.ExpenseTracker.entities.Income;
import com.EmaDeveloper.ExpenseTracker.repository.IncomeRepository;
import com.EmaDeveloper.ExpenseTracker.services.Income.IncomeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class IncomeServiceImplTest {

    @Mock
    private IncomeRepository incomeRepository;

    @InjectMocks
    private IncomeServiceImpl incomeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testPostIncome() {
        // Arrange
        IncomeDTO incomeDTO = new IncomeDTO();
        incomeDTO.setTitle("Salario");
        incomeDTO.setAmount(5000.0);
        incomeDTO.setCategory("Trabajo");
        incomeDTO.setDate(LocalDate.parse("2025-04-07"));
        incomeDTO.setDescription("Pago mensual del salario");

        Income savedIncome = new Income();
        savedIncome.setId(1L);
        savedIncome.setTitle(incomeDTO.getTitle());
        savedIncome.setAmount(incomeDTO.getAmount());
        savedIncome.setCategory(incomeDTO.getCategory());
        savedIncome.setDate(incomeDTO.getDate());
        savedIncome.setDescription(incomeDTO.getDescription());

        when(incomeRepository.save(any(Income.class))).thenReturn(savedIncome);

        // Act
        Income result = incomeService.postIncome(incomeDTO);

        // Assert
        assertNotNull(result);
        assertEquals("Salario", result.getTitle());
        assertEquals(5000.0, result.getAmount());
        verify(incomeRepository, times(1)).save(any(Income.class));
    }
}
