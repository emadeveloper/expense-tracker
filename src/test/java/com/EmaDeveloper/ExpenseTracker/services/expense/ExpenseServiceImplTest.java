package com.EmaDeveloper.ExpenseTracker.services.expense;

import com.EmaDeveloper.ExpenseTracker.expenses.dto.ExpenseRequestDTO;
import com.EmaDeveloper.ExpenseTracker.expenses.entities.Expense;
import com.EmaDeveloper.ExpenseTracker.expenses.services.ExpenseServiceImpl;
import com.EmaDeveloper.ExpenseTracker.expenses.repository.ExpenseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class ExpenseServiceImplTest {
    @Mock
    private ExpenseRepository expenseRepository;

    @InjectMocks
    private ExpenseServiceImpl expenseService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testPostExpense() {
        // Arrange
        ExpenseRequestDTO expenseDTO = new ExpenseRequestDTO();
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

    @Test
    void getAllExpenses_shouldReturnSortedExpensesByDateDescending() {
        // Arrange
        Expense expense1 = Expense.builder()
                .id(1L)
                .amount(1000.0)
                .date(LocalDate.parse("2025-04-01"))
                .category("Food")
                .title("Groceries")
                .description("Weekly groceries")
                .build();

        Expense expense2 = Expense.builder()
                .id(2L)
                .amount(2000.0)
                .date(LocalDate.parse("2025-04-05"))
                .category("Food")
                .title("Groceries")
                .description("Weekly groceries")
                .build();

        Expense expense3 = Expense.builder()
                .id(3L)
                .amount(4000.0)
                .date(LocalDate.parse("2025-04-10"))
                .category("Food")
                .title("Groceries")
                .description("Weekly groceries")
                .build();

        List<Expense> unorderedExpenses = Arrays.asList(expense1, expense2, expense3);

        when(expenseRepository.findAll()).thenReturn(unorderedExpenses);

        // Act
        List<Expense> result = expenseService.getAllExpenses();

        // Assert
        assertEquals(3, result.size());
        assertEquals(expense3, result.get(0)); // Fecha más reciente
        assertEquals(expense2, result.get(1));
        assertEquals(expense1, result.get(2)); // Fecha más antigua

        verify(expenseRepository, times(1)).findAll();
    }

    @Test
    void getExpenseById_shouldReturnExpense_whenIdExists() {
        // Arrange
        Long expenseId = 1L;
        Expense expense = new Expense();
        expense.setId(expenseId);
        expense.setTitle("Gym");
        expense.setAmount(3000.0);
        expense.setCategory("Health");
        expense.setDate(LocalDate.parse("2025-04-13"));
        expense.setDescription("Gym monthly fee");

        when(expenseRepository.findById(expenseId)).thenReturn(java.util.Optional.of(expense));

        // Act
        Expense result = expenseService.getExpenseById(expenseId);

        // Assert
        assertNotNull(result);
        assertEquals(expenseId, result.getId());
        assertEquals("Gym", result.getTitle());
        assertEquals(3000.0, result.getAmount());
        assertEquals("Health", result.getCategory());
        assertEquals(LocalDate.parse("2025-04-13"), result.getDate());
        assertEquals("Gym monthly fee", result.getDescription());

        verify(expenseRepository, times(1)).findById(expenseId);
    }

    @Test
    void updateExpense_shouldReturnUpdatedExpense_whenIdExists() {
        // Arrange
        Long expenseId = 1L;
        Expense expense = new Expense();
        expense.setId(expenseId);
        expense.setTitle("Gym");
        expense.setAmount(3000.0);
        expense.setCategory("Health");
        expense.setDate(LocalDate.parse("2025-04-13"));
        expense.setDescription("Gym monthly fee");

        ExpenseRequestDTO expenseDTO = new ExpenseRequestDTO();
        expenseDTO.setTitle("Updated Gym");
        expenseDTO.setAmount(3500.0);
        expenseDTO.setCategory("Health");
        expenseDTO.setDate(LocalDate.parse("2025-04-14"));
        expenseDTO.setDescription("Updated gym monthly fee");

        when(expenseRepository.findById(expenseId)).thenReturn(java.util.Optional.of(expense));
        when(expenseRepository.save(any(Expense.class))).thenReturn(expense);

        // Act
        Expense result = expenseService.updateExpense(expenseId, expenseDTO);

        // Assert
        assertNotNull(result);
        assertEquals(expenseId, result.getId());
        assertEquals("Updated Gym", result.getTitle());
        assertEquals(3500.0, result.getAmount());
        assertEquals("Health", result.getCategory());
        assertEquals(LocalDate.parse("2025-04-14"), result.getDate());
        assertEquals("Updated gym monthly fee", result.getDescription());

        verify(expenseRepository, times(1)).findById(expenseId);
        verify(expenseRepository, times(1)).save(any(Expense.class));
    }

    @Test
    void deleteExpense_shouldCallDeleteById_whenIdExists() {
        // Arrange
        Long expenseId = 1L;
        Expense expense = new Expense();
        expense.setId(expenseId);
        expense.setTitle("Gym");
        expense.setAmount(3000.0);
        expense.setCategory("Health");
        expense.setDate(LocalDate.parse("2025-04-13"));
        expense.setDescription("Gym monthly fee");

        when(expenseRepository.findById(expenseId)).thenReturn(java.util.Optional.of(expense));

        // Act
        expenseService.deleteExpense(expenseId);


        // Assert
        verify(expenseRepository, times(1)).deleteById(expenseId);
    }
}
