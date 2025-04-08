package com.EmaDeveloper.ExpenseTracker.services;

import com.EmaDeveloper.ExpenseTracker.dto.ExpenseDTO;
import com.EmaDeveloper.ExpenseTracker.entities.Expense;
import com.EmaDeveloper.ExpenseTracker.repository.ExpenseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class ExpenseServiceImplTest {
    @Mock
    private ExpenseRepository expenseRepository;

    @InjectMocks
    private ExpenseServiceImpl expenseService;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testPostExpense() {
        // Arrange
        ExpenseDTO expenseDTO = new ExpenseDTO();
        expenseDTO.setTitle("Gimnasio");
        expenseDTO.setAmount(3000.0);
        expenseDTO.setCategory("Salud");
        expenseDTO.setDate(LocalDate.parse("2025-04-07"));
        expenseDTO.setDescription("Cuota mensual del gym");

        Expense savedExpense = new Expense();
        savedExpense.setId(1L);
        savedExpense.setTitle(expenseDTO.getTitle());
        savedExpense.setAmount(expenseDTO.getAmount());
        savedExpense.setCategory(expenseDTO.getCategory());
        savedExpense.setDate(expenseDTO.getDate());
        savedExpense.setDescription(expenseDTO.getDescription());

        when(expenseRepository.save(any(Expense.class))).thenReturn(savedExpense);

        // Act
        Expense result = expenseService.postExpense(expenseDTO);

        // Assert
        assertNotNull(result);
        assertEquals("Gimnasio", result.getTitle());
        assertEquals(3000.0, result.getAmount());
        verify(expenseRepository, times(1)).save(any(Expense.class));
    }
}
