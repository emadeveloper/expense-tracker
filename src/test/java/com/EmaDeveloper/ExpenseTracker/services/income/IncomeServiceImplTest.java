package com.EmaDeveloper.ExpenseTracker.services.income;

import com.EmaDeveloper.ExpenseTracker.incomes.dto.IncomeRequestDTO;
import com.EmaDeveloper.ExpenseTracker.incomes.entities.Income;
import com.EmaDeveloper.ExpenseTracker.incomes.respository.IncomeRepository;
import com.EmaDeveloper.ExpenseTracker.incomes.services.IncomeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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
    void testGetAllIncomes_shouldReturnAllIncomesSortedDescending() {
        // Arrange
        Income income1 = Income.builder()
                .id(1L)
                .title("Salario")
                .amount(5000.0)
                .category("Trabajo")
                .date(LocalDate.parse("2025-04-07"))
                .description("Pago mensual del salario")
                .build();

        Income income2 = Income.builder()
                .id(2L)
                .title("Salario")
                .amount(3000.0)
                .category("Trabajo")
                .date(LocalDate.parse("2025-04-14"))
                .description("Pago mensual del salario")
                .build();

        Income income3 = Income.builder()
                .id(3L)
                .title("Salario")
                .amount(2000.0)
                .category("Trabajo")
                .date(LocalDate.parse("2025-04-13"))
                .description("Pago mensual del salario")
                .build();

        List<Income> unorderedIncomes = List.of(income1, income2, income3);

        when(incomeRepository.findAll()).thenReturn(unorderedIncomes);

        // Act
        List<IncomeRequestDTO> result = incomeService.getAllIncomes();

        // Assert
        assertNotNull(result);
        assertEquals(3, result.size());

        assertEquals(income2.getId(), result.get(0).getId()); // Más reciente: 2025-04-14
        assertEquals(income3.getId(), result.get(1).getId()); // Luego: 2025-04-13
        assertEquals(income1.getId(), result.get(2).getId()); // Más viejo: 2025-04-07

        verify(incomeRepository, times(1)).findAll();
    }

    @Test
    void testGetIncomeById_shouldReturnIncome() {
        // Arrange
        Long incomeId = 1L;
        Income income = Income.builder()
                .id(incomeId)
                .title("Salario")
                .amount(5000.0)
                .category("Trabajo")
                .date(LocalDate.parse("2025-04-10"))
                .description("Pago mensual del salario")
                .build();

        when(incomeRepository.findById(incomeId)).thenReturn(Optional.of(income));

        // Act
        Income result = incomeService.getIncomeById(incomeId);

        // Assert
        assertNotNull(result);
        assertEquals(incomeId, result.getId());
        assertEquals("Salario", result.getTitle());
        assertEquals("Trabajo", result.getCategory());
        assertEquals(5000.0, result.getAmount());
        assertEquals(LocalDate.parse("2025-04-10"), result.getDate());
        assertEquals("Pago mensual del salario", result.getDescription());

        verify(incomeRepository, times(1)).findById(incomeId);
    }

    @Test
    void testPostIncome() {
        // Arrange
        IncomeRequestDTO incomeDTO = new IncomeRequestDTO();
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

    @Test
    void testUpdateIncome_shouldReturnUpdatedIncome() {
        // Arrange
        Long incomeId = 1L;
        IncomeRequestDTO incomeDTO = new IncomeRequestDTO();
        incomeDTO.setId(incomeId);
        incomeDTO.setTitle("Salario");
        incomeDTO.setAmount(5000.0);
        incomeDTO.setDate(LocalDate.parse("2025-04-15"));
        incomeDTO.setCategory("Trabajo");
        incomeDTO.setDescription("Pago mensual del salario");

        Income existingIncome = new Income();
        existingIncome.setId(incomeId);
        existingIncome.setTitle("Viejo Título");
        existingIncome.setAmount(1000.0);
        existingIncome.setDate(LocalDate.parse("2025-01-01"));
        existingIncome.setCategory("Vieja Categoría");
        existingIncome.setDescription("Descripción vieja");

        when(incomeRepository.findById(incomeId)).thenReturn(Optional.of(existingIncome));
        when(incomeRepository.save(any(Income.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Income result = incomeService.updateIncome(incomeId, incomeDTO);

        // Assert
        assertNotNull(result);
        assertEquals(incomeId, result.getId());
        assertEquals("Salario", result.getTitle());
        assertEquals("Trabajo", result.getCategory());
        assertEquals(5000.0, result.getAmount());
        assertEquals(LocalDate.parse("2025-04-15"), result.getDate());
        assertEquals("Pago mensual del salario", result.getDescription());

        verify(incomeRepository, times(1)).findById(incomeId);
        verify(incomeRepository, times(1)).save(any(Income.class));
    }

    @Test
    void deleteIncome_shouldDeleteIncome(){
        // Arrange
        Long incomeId = 1L;
        Income income = Income.builder()
                .id(incomeId)
                .title("Salario")
                .amount(5000.0)
                .category("Trabajo")
                .date(LocalDate.parse("2025-04-10"))
                .description("Pago mensual del salario")
                .build();

        when(incomeRepository.findById(incomeId)).thenReturn(Optional.of(income));

        // Act
        incomeService.deleteIncome(incomeId);

        // Assert
        verify(incomeRepository, times(1)).deleteById(incomeId);
    }
}
