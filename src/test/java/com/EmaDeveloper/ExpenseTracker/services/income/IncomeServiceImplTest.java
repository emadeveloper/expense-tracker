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
import java.util.List;

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
        List<IncomeDTO> result = incomeService.getAllIncomes();

        // Assert
        assertNotNull(result);
        assertEquals(3, result.size());

        assertEquals(income2.getId(), result.get(0).getId()); // Más reciente: 2025-04-14
        assertEquals(income3.getId(), result.get(1).getId()); // Luego: 2025-04-13
        assertEquals(income1.getId(), result.get(2).getId()); // Más viejo: 2025-04-07

        verify(incomeRepository, times(1)).findAll();
    }

}
