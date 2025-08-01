//package com.EmaDeveloper.ExpenseTracker.stats.services;
//
//import com.EmaDeveloper.ExpenseTracker.auth.services.AuthService;
//import com.EmaDeveloper.ExpenseTracker.expenses.entities.Expense;
//import com.EmaDeveloper.ExpenseTracker.incomes.entities.Income;
//import com.EmaDeveloper.ExpenseTracker.expenses.repository.ExpenseRepository;
//import com.EmaDeveloper.ExpenseTracker.incomes.respository.IncomeRepository;
//import com.EmaDeveloper.ExpenseTracker.users.entities.User;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import java.util.Optional;
//
//import java.time.LocalDate;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//class StatsServiceImplTest {
//
//    @Mock
//    private IncomeRepository incomeRepository;
//
//    @Mock
//    private ExpenseRepository expenseRepository;
//
//    @Mock
//    AuthService authService;
//
//    @InjectMocks
//    private StatsServiceImpl statsServiceImpl;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);}
//
//    @Test
//    void testGetChartData_withAnyDates_shouldReturnCorrectChartDetails() {
//        // Arrange
//        User user = User.builder()
//                .id(1L)
//                .username("testUser")
//                .email("test@email.com")
//                .password("password")
//                .build();
//
//        Income income1 = Income.builder()
//                .id(1L)
//                .title("Salario")
//                .amount(5000.0)
//                .category("Trabajo")
//                .date(LocalDate.of(2025, 4, 15))
//                .description("Pago mensual del salario")
//                .build();
//
//        Expense expense1 = Expense.builder()
//                .id(1L)
//                .title("Alquiler")
//                .amount(750.0)
//                .category("Gastos fijos")
//                .date(LocalDate.of(2025, 4, 15))
//                .description("Pago mensual del alquiler")
//                .build();
//
//        when(incomeRepository.findByUserAndDateBetween(user, LocalDate.now(), any(LocalDate.class)))
//                .thenReturn(List.of(income1));
//        when(expenseRepository.findByUserAndDateBetween(user, LocalDate.now(), any(LocalDate.class)))
//                .thenReturn(List.of(expense1));
//
//        // Act
//        var result = statsServiceImpl.getChartData();
//
//        // Assert
//        assertNotNull(result);
//        assertEquals(1, result.getIncomeList().size());
//        assertEquals(1, result.getExpenseList().size());
//        assertEquals("Trabajo", result.getIncomeList().get(0).getCategory());
//        assertEquals("Gastos fijos", result.getExpenseList().get(0).getCategory());
//
//        verify(incomeRepository).findByUserAndDateBetween(user, LocalDate.now(), any(LocalDate.class));
//        verify(expenseRepository).findByUserAndDateBetween(user, LocalDate.now(), any(LocalDate.class));
//    }
//
//    @Test
//    void getStats_shouldReturnLatestIncomeAndExpenseStats() {
//        // Arrange
//        User user = User.builder()
//                .id(1L)
//                .username("testUser")
//                .email("test@email.com")
//                .password("password")
//                .build();
//
//        Income income1 = Income.builder()
//                .id(1L)
//                .title("Salario")
//                .amount(5000.0)
//                .category("Trabajo")
//                .date(LocalDate.of(2025, 4, 15))
//                .description("Pago mensual del salario")
//                .build();
//
//        Income income2 = Income.builder()
//                .id(2L)
//                .title("Salario2")
//                .amount(7000.0)
//                .category("Trabajo")
//                .date(LocalDate.of(2025, 4, 17))
//                .description("Pago mensual del salario")
//                .build();
//
//        Expense expense1 = Expense.builder()
//                .id(1L)
//                .title("Alquiler")
//                .amount(750.0)
//                .category("Gastos fijos")
//                .date(LocalDate.of(2025, 4, 15))
//                .description("Pago mensual del alquiler")
//                .build();
//
//        Expense expense2 = Expense.builder()
//                .id(2L)
//                .title("Alquiler")
//                .amount(1050.0)
//                .category("Gastos fijos")
//                .date(LocalDate.of(2025, 4, 18))
//                .description("Pago mensual del alquiler")
//                .build();
//
//
//        when(authService.getCurrentUser()).thenReturn(user);
//
//
//        when(incomeRepository.sumAmountByUser(user)).thenReturn(12000.0);
//        when(expenseRepository.sumAmountByUser(user)).thenReturn(1800.0);
//
//        when(incomeRepository.findFirstByUserOrderByDateDesc(user)).thenReturn(Optional.ofNullable(income2));
//        when(expenseRepository.findFirstByUserOrderByDateDesc(user)).thenReturn(Optional.ofNullable(expense2));
//
//        when(incomeRepository.findAll()).thenReturn(List.of(income1, income2));
//        when(expenseRepository.findAll()).thenReturn(List.of(expense1, expense2));
//
//        // Act
//        var result = statsServiceImpl.getStats();
//
//        // Assert
//        assertNotNull(result);
//        assertNotNull(result.getLatestIncome());
//        assertNotNull(result.getLatestExpense());
//        assertEquals(12000.0, result.getIncome());
//        assertEquals(1800.0, result.getExpense());
//        assertEquals("Salario2", result.getLatestIncome().getTitle());
//        assertEquals("Alquiler", result.getLatestExpense().getTitle());
//
//        assertEquals(5000.0, result.getMinIncome());
//        assertEquals(7000.0, result.getMaxIncome());
//
//        assertEquals(750.0, result.getMinExpense());
//        assertEquals(1050.0, result.getMaxExpense());
//
//
//        verify(incomeRepository).sumAmountByUser(user);
//        verify(expenseRepository).sumAmountByUser(user);
//        verify(incomeRepository).findFirstByUserOrderByDateDesc(user);
//        verify(expenseRepository).findFirstByUserOrderByDateDesc(user);
//    }
//}
//
