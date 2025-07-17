//package com.expensetracker.service;
//
//import com.EmaDeveloper.ExpenseTracker.auth.services.AuthService;
//import com.EmaDeveloper.ExpenseTracker.exceptions.custom.ExpenseNotFoundException;
//import com.EmaDeveloper.ExpenseTracker.expenses.services.ExpenseServiceImpl;
//import com.EmaDeveloper.ExpenseTracker.expenses.dto.ExpenseRequestDTO;
//import com.EmaDeveloper.ExpenseTracker.expenses.dto.ExpenseResponseDTO;
//import com.EmaDeveloper.ExpenseTracker.expenses.entities.Expense;
//import com.EmaDeveloper.ExpenseTracker.expenses.repository.ExpenseRepository;
//import com.EmaDeveloper.ExpenseTracker.users.entities.User;
//import com.expensetracker.dto.ExpenseRequestDTO;
//import com.expensetracker.dto.ExpenseResponseDTO;
//import com.expensetracker.entity.Expense;
//import com.expensetracker.entity.User;
//import com.expensetracker.exception.ExpenseNotFoundException;
//import com.expensetracker.mapper.ExpenseMapper;
//import com.expensetracker.repository.ExpenseRepository;
//import com.expensetracker.service.impl.ExpenseServiceImpl;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Nested;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//
//import java.time.LocalDate;
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.eq;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//@DisplayName("ExpenseService Tests")
//class ExpenseServiceImplTest {
//
//    @Mock
//    private ExpenseRepository expenseRepository;
//
//    @Mock
//    private AuthService authService;
//
//    @InjectMocks
//    private ExpenseServiceImpl expenseService;
//
//    private User currentUser;
//    private User otherUser;
//    private ExpenseRequestDTO validExpenseDTO;
//    private Expense validExpense;
//    private Expense savedExpense;
//
//    @BeforeEach
//    void setUp() {
//        // Setup current user
//        currentUser = new User();
//        currentUser.setId(1L);
//        currentUser.setUsername("testuser");
//        currentUser.setEmail("test@example.com");
//
//        // Setup other user for security tests
//        otherUser = new User();
//        otherUser.setId(2L);
//        otherUser.setUsername("otheruser");
//        otherUser.setEmail("other@example.com");
//
//        // Setup valid expense DTO
//        validExpenseDTO = new ExpenseRequestDTO();
//        validExpenseDTO.setTitle("Gimnasio");
//        validExpenseDTO.setAmount(3000.0);
//        validExpenseDTO.setCategory("Salud");
//        validExpenseDTO.setDate(LocalDate.of(2025, 4, 7));
//        validExpenseDTO.setDescription("Cuota mensual del gym");
//
//        // Setup valid expense entity
//        validExpense = new Expense();
//        validExpense.setId(1L);
//        validExpense.setTitle("Gimnasio");
//        validExpense.setAmount(3000.0);
//        validExpense.setCategory("Salud");
//        validExpense.setDate(LocalDate.of(2025, 4, 7));
//        validExpense.setDescription("Cuota mensual del gym");
//        validExpense.setUser(currentUser);
//
//        // Setup saved expense (with ID)
//        savedExpense = new Expense();
//        savedExpense.setId(1L);
//        savedExpense.setTitle(validExpenseDTO.getTitle());
//        savedExpense.setAmount(validExpenseDTO.getAmount());
//        savedExpense.setCategory(validExpenseDTO.getCategory());
//        savedExpense.setDate(validExpenseDTO.getDate());
//        savedExpense.setDescription(validExpenseDTO.getDescription());
//        savedExpense.setUser(currentUser);
//    }
//
//    @Nested
//    @DisplayName("postExpense Tests")
//    class PostExpenseTests {
//
//        @Test
//        @DisplayName("Should create expense successfully with valid data")
//        void postExpense_shouldCreateExpense_whenValidData() {
//            // Arrange
//            when(authService.getCurrentUser()).thenReturn(currentUser);
//            when(expenseRepository.save(any(Expense.class))).thenReturn(savedExpense);
//
//            // Act
//            ExpenseResponseDTO result = expenseService.postExpense(validExpenseDTO);
//
//            // Assert
//            assertNotNull(result);
//            assertEquals("Gimnasio", result.getTitle());
//            assertEquals(3000.0, result.getAmount());
//            assertEquals("Salud", result.getCategory());
//            assertEquals(LocalDate.of(2025, 4, 7), result.getDate());
//            assertEquals("Cuota mensual del gym", result.getDescription());
//
//            // Verify interactions
//            verify(authService, times(1)).getCurrentUser();
//            verify(expenseRepository, times(1)).save(any(Expense.class));
//        }
//
//        @Test
//        @DisplayName("Should throw exception when user is null")
//        void postExpense_shouldThrowException_whenUserIsNull() {
//            // Arrange
//            when(authService.getCurrentUser()).thenReturn(null);
//
//            // Act & Assert
//            assertThrows(IllegalStateException.class, () -> {
//                expenseService.postExpense(validExpenseDTO);
//            });
//
//            verify(authService, times(1)).getCurrentUser();
//            verify(expenseRepository, never()).save(any(Expense.class));
//        }
//
//        @Test
//        @DisplayName("Should handle repository save failure")
//        void postExpense_shouldThrowException_whenRepositoryFails() {
//            // Arrange
//            when(authService.getCurrentUser()).thenReturn(currentUser);
//            when(expenseRepository.save(any(Expense.class)))
//                    .thenThrow(new RuntimeException("Database error"));
//
//            // Act & Assert
//            assertThrows(RuntimeException.class, () -> {
//                expenseService.postExpense(validExpenseDTO);
//            });
//
//            verify(authService, times(1)).getCurrentUser();
//            verify(expenseRepository, times(1)).save(any(Expense.class));
//        }
//
//        @Test
//        @DisplayName("Should create expense with minimum required fields")
//        void postExpense_shouldCreateExpense_withMinimumFields() {
//            // Arrange
//            ExpenseRequestDTO minimalDTO = new ExpenseRequestDTO();
//            minimalDTO.setTitle("Test");
//            minimalDTO.setAmount(100.0);
//            minimalDTO.setCategory("Test");
//            minimalDTO.setDate(LocalDate.now());
//
//            Expense minimalExpense = new Expense();
//            minimalExpense.setId(1L);
//            minimalExpense.setTitle("Test");
//            minimalExpense.setAmount(100.0);
//            minimalExpense.setCategory("Test");
//            minimalExpense.setDate(LocalDate.now());
//            minimalExpense.setUser(currentUser);
//
//            when(authService.getCurrentUser()).thenReturn(currentUser);
//            when(expenseRepository.save(any(Expense.class))).thenReturn(minimalExpense);
//
//            // Act
//            ExpenseResponseDTO result = expenseService.postExpense(minimalDTO);
//
//            // Assert
//            assertNotNull(result);
//            assertEquals("Test", result.getTitle());
//            assertEquals(100.0, result.getAmount());
//            assertNull(result.getDescription()); // Description can be null
//        }
//    }
//
//    @Nested
//    @DisplayName("deleteExpense Tests")
//    class DeleteExpenseTests {
//
//        @Test
//        @DisplayName("Should delete expense successfully when user owns it")
//        void deleteExpense_shouldDeleteExpense_whenUserOwnsIt() {
//            // Arrange
//            Long expenseId = 1L;
//            when(expenseRepository.existsById(expenseId)).thenReturn(true);
//            when(authService.getCurrentUser()).thenReturn(currentUser);
//            when(expenseRepository.findById(expenseId)).thenReturn(Optional.of(validExpense));
//
//            // Act
//            expenseService.deleteExpense(expenseId);
//
//            // Assert
//            verify(expenseRepository, times(1)).existsById(expenseId);
//            verify(authService, times(1)).getCurrentUser();
//            verify(expenseRepository, times(1)).findById(expenseId);
//            verify(expenseRepository, times(1)).deleteById(expenseId);
//        }
//
//        @Test
//        @DisplayName("Should throw ExpenseNotFoundException when expense doesn't exist")
//        void deleteExpense_shouldThrowExpenseNotFoundException_whenExpenseDoesNotExist() {
//            // Arrange
//            Long expenseId = 999L;
//            when(expenseRepository.existsById(expenseId)).thenReturn(false);
//
//            // Act & Assert
//            ExpenseNotFoundException exception = assertThrows(ExpenseNotFoundException.class, () -> {
//                expenseService.deleteExpense(expenseId);
//            });
//
//            assertEquals(expenseId, exception.getId());
//            verify(expenseRepository, times(1)).existsById(expenseId);
//            verify(authService, never()).getCurrentUser();
//            verify(expenseRepository, never()).findById(expenseId);
//            verify(expenseRepository, never()).deleteById(expenseId);
//        }
//
//        @Test
//        @DisplayName("Should throw SecurityException when user doesn't own expense")
//        void deleteExpense_shouldThrowSecurityException_whenUserDoesNotOwnExpense() {
//            // Arrange
//            Long expenseId = 1L;
//            Expense otherUserExpense = new Expense();
//            otherUserExpense.setId(expenseId);
//            otherUserExpense.setUser(otherUser);
//
//            when(expenseRepository.existsById(expenseId)).thenReturn(true);
//            when(authService.getCurrentUser()).thenReturn(currentUser);
//            when(expenseRepository.findById(expenseId)).thenReturn(Optional.of(otherUserExpense));
//
//            // Act & Assert
//            SecurityException exception = assertThrows(SecurityException.class, () -> {
//                expenseService.deleteExpense(expenseId);
//            });
//
//            assertEquals("You do not have permission to delete this expense", exception.getMessage());
//            verify(expenseRepository, times(1)).existsById(expenseId);
//            verify(authService, times(1)).getCurrentUser();
//            verify(expenseRepository, times(1)).findById(expenseId);
//            verify(expenseRepository, never()).deleteById(expenseId);
//        }
//
//        @Test
//        @DisplayName("Should throw exception when current user is null")
//        void deleteExpense_shouldThrowException_whenCurrentUserIsNull() {
//            // Arrange
//            Long expenseId = 1L;
//            when(expenseRepository.existsById(expenseId)).thenReturn(true);
//            when(authService.getCurrentUser()).thenReturn(null);
//
//            // Act & Assert
//            assertThrows(IllegalStateException.class, () -> {
//                expenseService.deleteExpense(expenseId);
//            });
//
//            verify(expenseRepository, times(1)).existsById(expenseId);
//            verify(authService, times(1)).getCurrentUser();
//            verify(expenseRepository, never()).deleteById(expenseId);
//        }
//
//        @Test
//        @DisplayName("Should handle repository findById returning empty optional")
//        void deleteExpense_shouldHandleEmptyOptional_whenExpenseNotFoundAfterExistsCheck() {
//            // Arrange - Race condition scenario
//            Long expenseId = 1L;
//            when(expenseRepository.existsById(expenseId)).thenReturn(true);
//            when(authService.getCurrentUser()).thenReturn(currentUser);
//            when(expenseRepository.findById(expenseId)).thenReturn(Optional.empty());
//
//            // Act
//            expenseService.deleteExpense(expenseId);
//
//            // Assert - Should still proceed to delete (idempotent operation)
//            verify(expenseRepository, times(1)).deleteById(expenseId);
//        }
//    }
//
//    @Nested
//    @DisplayName("getExpenseById Tests")
//    class GetExpenseByIdTests {
//
//        @Test
//        @DisplayName("Should return expense when found and user owns it")
//        void getExpenseById_shouldReturnExpense_whenFoundAndUserOwnsIt() {
//            // Arrange
//            Long expenseId = 1L;
//            when(expenseRepository.findById(expenseId)).thenReturn(Optional.of(validExpense));
//            when(authService.getCurrentUser()).thenReturn(currentUser);
//
//            // Act
//            ExpenseResponseDTO result = expenseService.getExpenseById(expenseId);
//
//            // Assert
//            assertNotNull(result);
//            assertEquals(validExpense.getId(), result.getId());
//            assertEquals(validExpense.getTitle(), result.getTitle());
//            assertEquals(validExpense.getAmount(), result.getAmount());
//
//            verify(expenseRepository, times(1)).findById(expenseId);
//            verify(authService, times(1)).getCurrentUser();
//        }
//
//        @Test
//        @DisplayName("Should throw ExpenseNotFoundException when expense not found")
//        void getExpenseById_shouldThrowExpenseNotFoundException_whenNotFound() {
//            // Arrange
//            Long expenseId = 999L;
//            when(expenseRepository.findById(expenseId)).thenReturn(Optional.empty());
//
//            // Act & Assert
//            ExpenseNotFoundException exception = assertThrows(ExpenseNotFoundException.class, () -> {
//                expenseService.getExpenseById(expenseId);
//            });
//
//            assertEquals(expenseId, exception.getId());
//            verify(expenseRepository, times(1)).findById(expenseId);
//            verify(authService, never()).getCurrentUser();
//        }
//
//        @Test
//        @DisplayName("Should throw SecurityException when user doesn't own expense")
//        void getExpenseById_shouldThrowSecurityException_whenUserDoesNotOwnExpense() {
//            // Arrange
//            Long expenseId = 1L;
//            Expense otherUserExpense = new Expense();
//            otherUserExpense.setId(expenseId);
//            otherUserExpense.setUser(otherUser);
//
//            when(expenseRepository.findById(expenseId)).thenReturn(Optional.of(otherUserExpense));
//            when(authService.getCurrentUser()).thenReturn(currentUser);
//
//            // Act & Assert
//            SecurityException exception = assertThrows(SecurityException.class, () -> {
//                expenseService.getExpenseById(expenseId);
//            });
//
//            assertEquals("You do not have permission to view this expense", exception.getMessage());
//            verify(expenseRepository, times(1)).findById(expenseId);
//            verify(authService, times(1)).getCurrentUser();
//        }
//    }
//
//    @Nested
//    @DisplayName("getAllExpenses Tests")
//    class GetAllExpensesTests {
//
//        @Test
//        @DisplayName("Should return paginated expenses for current user")
//        void getAllExpenses_shouldReturnPaginatedExpenses_forCurrentUser() {
//            // Arrange
//            Pageable pageable = PageRequest.of(0, 10);
//            List<Expense> expenses = Arrays.asList(validExpense, createSecondExpense());
//            Page<Expense> expensePage = new PageImpl<>(expenses, pageable, expenses.size());
//
//            when(authService.getCurrentUser()).thenReturn(currentUser);
//            when(expenseRepository.findByUser(currentUser, pageable)).thenReturn(expensePage);
//
//            // Act
//            Page<ExpenseResponseDTO> result = expenseService.getAllExpenses(pageable);
//
//            // Assert
//            assertNotNull(result);
//            assertEquals(2, result.getContent().size());
//            assertEquals(expenses.size(), result.getTotalElements());
//
//            verify(authService, times(1)).getCurrentUser();
//            verify(expenseRepository, times(1)).findByUser(currentUser, pageable);
//        }
//
//        @Test
//        @DisplayName("Should return empty page when user has no expenses")
//        void getAllExpenses_shouldReturnEmptyPage_whenUserHasNoExpenses() {
//            // Arrange
//            Pageable pageable = PageRequest.of(0, 10);
//            Page<Expense> emptyPage = new PageImpl<>(Collections.emptyList(), pageable, 0);
//
//            when(authService.getCurrentUser()).thenReturn(currentUser);
//            when(expenseRepository.findByUser(currentUser, pageable)).thenReturn(emptyPage);
//
//            // Act
//            Page<ExpenseResponseDTO> result = expenseService.getAllExpenses(pageable);
//
//            // Assert
//            assertNotNull(result);
//            assertTrue(result.getContent().isEmpty());
//            assertEquals(0, result.getTotalElements());
//
//            verify(authService, times(1)).getCurrentUser();
//            verify(expenseRepository, times(1)).findByUser(currentUser, pageable);
//        }
//
//        @Test
//        @DisplayName("Should throw exception when current user is null")
//        void getAllExpenses_shouldThrowException_whenCurrentUserIsNull() {
//            // Arrange
//            Pageable pageable = PageRequest.of(0, 10);
//            when(authService.getCurrentUser()).thenReturn(null);
//
//            // Act & Assert
//            assertThrows(IllegalStateException.class, () -> {
//                expenseService.getAllExpenses(pageable);
//            });
//
//            verify(authService, times(1)).getCurrentUser();
//            verify(expenseRepository, never()).findByUser(any(), any());
//        }
//    }
//
//    @Nested
//    @DisplayName("updateExpense Tests")
//    class UpdateExpenseTests {
//
//        @Test
//        @DisplayName("Should update expense successfully when user owns it")
//        void updateExpense_shouldUpdateExpense_whenUserOwnsIt() {
//            // Arrange
//            Long expenseId = 1L;
//            ExpenseRequestDTO updateDTO = new ExpenseRequestDTO();
//            updateDTO.setTitle("Updated Gym");
//            updateDTO.setAmount(3500.0);
//            updateDTO.setCategory("Fitness");
//            updateDTO.setDate(LocalDate.of(2025, 5, 7));
//            updateDTO.setDescription("Updated gym membership");
//
//            Expense updatedExpense = new Expense();
//            updatedExpense.setId(expenseId);
//            updatedExpense.setTitle(updateDTO.getTitle());
//            updatedExpense.setAmount(updateDTO.getAmount());
//            updatedExpense.setCategory(updateDTO.getCategory());
//            updatedExpense.setDate(updateDTO.getDate());
//            updatedExpense.setDescription(updateDTO.getDescription());
//            updatedExpense.setUser(currentUser);
//
//            when(expenseRepository.findById(expenseId)).thenReturn(Optional.of(validExpense));
//            when(authService.getCurrentUser()).thenReturn(currentUser);
//            when(expenseRepository.save(any(Expense.class))).thenReturn(updatedExpense);
//
//            // Act
//            ExpenseResponseDTO result = expenseService.updateExpense(expenseId, updateDTO);
//
//            // Assert
//            assertNotNull(result);
//            assertEquals("Updated Gym", result.getTitle());
//            assertEquals(3500.0, result.getAmount());
//            assertEquals("Fitness", result.getCategory());
//
//            verify(expenseRepository, times(1)).findById(expenseId);
//            verify(authService, times(1)).getCurrentUser();
//            verify(expenseRepository, times(1)).save(any(Expense.class));
//        }
//
//        @Test
//        @DisplayName("Should throw ExpenseNotFoundException when expense not found")
//        void updateExpense_shouldThrowExpenseNotFoundException_whenNotFound() {
//            // Arrange
//            Long expenseId = 999L;
//            when(expenseRepository.findById(expenseId)).thenReturn(Optional.empty());
//
//            // Act & Assert
//            ExpenseNotFoundException exception = assertThrows(ExpenseNotFoundException.class, () -> {
//                expenseService.updateExpense(expenseId, validExpenseDTO);
//            });
//
//            assertEquals(expenseId, exception.getId());
//            verify(expenseRepository, times(1)).findById(expenseId);
//            verify(authService, never()).getCurrentUser();
//            verify(expenseRepository, never()).save(any(Expense.class));
//        }
//
//        @Test
//        @DisplayName("Should throw SecurityException when user doesn't own expense")
//        void updateExpense_shouldThrowSecurityException_whenUserDoesNotOwnExpense() {
//            // Arrange
//            Long expenseId = 1L;
//            Expense otherUserExpense = new Expense();
//            otherUserExpense.setId(expenseId);
//            otherUserExpense.setUser(otherUser);
//
//            when(expenseRepository.findById(expenseId)).thenReturn(Optional.of(otherUserExpense));
//            when(authService.getCurrentUser()).thenReturn(currentUser);
//
//            // Act & Assert
//            SecurityException exception = assertThrows(SecurityException.class, () -> {
//                expenseService.updateExpense(expenseId, validExpenseDTO);
//            });
//
//            assertEquals("You do not have permission to update this expense", exception.getMessage());
//            verify(expenseRepository, times(1)).findById(expenseId);
//            verify(authService, times(1)).getCurrentUser();
//            verify(expenseRepository, never()).save(any(Expense.class));
//        }
//    }
//
//    @Nested
//    @DisplayName("getExpensesByCategory Tests")
//    class GetExpensesByCategoryTests {
//
//        @Test
//        @DisplayName("Should return expenses filtered by category for current user")
//        void getExpensesByCategory_shouldReturnFilteredExpenses_forCurrentUser() {
//            // Arrange
//            String category = "Salud";
//            Pageable pageable = PageRequest.of(0, 10);
//            List<Expense> healthExpenses = Arrays.asList(validExpense);
//            Page<Expense> expensePage = new PageImpl<>(healthExpenses, pageable, healthExpenses.size());
//
//            when(authService.getCurrentUser()).thenReturn(currentUser);
//            when(expenseRepository.findByUserAndCategory(currentUser, category, pageable))
//                    .thenReturn(expensePage);
//
//            // Act
//            Page<ExpenseResponseDTO> result = expenseService.getExpensesByCategory(category, pageable);
//
//            // Assert
//            assertNotNull(result);
//            assertEquals(1, result.getContent().size());
//            assertEquals(category, result.getContent().get(0).getCategory());
//
//            verify(authService, times(1)).getCurrentUser();
//            verify(expenseRepository, times(1)).findByUserAndCategory(currentUser, category, pageable);
//        }
//
//        @Test
//        @DisplayName("Should return empty page when no expenses in category")
//        void getExpensesByCategory_shouldReturnEmptyPage_whenNoExpensesInCategory() {
//            // Arrange
//            String category = "NonExistentCategory";
//            Pageable pageable = PageRequest.of(0, 10);
//            Page<Expense> emptyPage = new PageImpl<>(Collections.emptyList(), pageable, 0);
//
//            when(authService.getCurrentUser()).thenReturn(currentUser);
//            when(expenseRepository.findByUserAndCategory(currentUser, category, pageable))
//                    .thenReturn(emptyPage);
//
//            // Act
//            Page<ExpenseResponseDTO> result = expenseService.getExpensesByCategory(category, pageable);
//
//            // Assert
//            assertNotNull(result);
//            assertTrue(result.getContent().isEmpty());
//            assertEquals(0, result.getTotalElements());
//        }
//    }
//
//    @Nested
//    @DisplayName("getExpensesByDateRange Tests")
//    class GetExpensesByDateRangeTests {
//
//        @Test
//        @DisplayName("Should return expenses within date range for current user")
//        void getExpensesByDateRange_shouldReturnExpensesInRange_forCurrentUser() {
//            // Arrange
//            LocalDate startDate = LocalDate.of(2025, 4, 1);
//            LocalDate endDate = LocalDate.of(2025, 4, 30);
//            Pageable pageable = PageRequest.of(0, 10);
//            List<Expense> expensesInRange = Arrays.asList(validExpense);
//            Page<Expense> expensePage = new PageImpl<>(expensesInRange, pageable, expensesInRange.size());
//
//            when(authService.getCurrentUser()).thenReturn(currentUser);
//            when(expenseRepository.findByUserAndDateBetween(currentUser, startDate, endDate, pageable))
//                    .thenReturn(expensePage);
//
//            // Act
//            Page<ExpenseResponseDTO> result = expenseService.getExpensesByDateRange(startDate, endDate, pageable);
//
//            // Assert
//            assertNotNull(result);
//            assertEquals(1, result.getContent().size());
//            assertTrue(result.getContent().get(0).getDate().isAfter(startDate.minusDays(1)));
//            assertTrue(result.getContent().get(0).getDate().isBefore(endDate.plusDays(1)));
//
//            verify(authService, times(1)).getCurrentUser();
//            verify(expenseRepository, times(1)).findByUserAndDateBetween(currentUser, startDate, endDate, pageable);
//        }
//
//        @Test
//        @DisplayName("Should throw exception when start date is after end date")
//        void getExpensesByDateRange_shouldThrowException_whenStartDateAfterEndDate() {
//            // Arrange
//            LocalDate startDate = LocalDate.of(2025, 4, 30);
//            LocalDate endDate = LocalDate.of(2025, 4, 1);
//            Pageable pageable = PageRequest.of(0, 10);
//
//            // Act & Assert
//            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
//                expenseService.getExpensesByDateRange(startDate, endDate, pageable);
//            });
//
//            assertEquals("Start date must be before or equal to end date", exception.getMessage());
//            verify(authService, never()).getCurrentUser();
//            verify(expenseRepository, never()).findByUserAndDateBetween(any(), any(), any(), any());
//        }
//    }
//
//    // Helper method to create a second expense for testing
//    private Expense createSecondExpense() {
//        Expense expense = new Expense();
//        expense.setId(2L);
//        expense.setTitle("Supermercado");
//        expense.setAmount(5000.0);
//        expense.setCategory("Alimentación");
//        expense.setDate(LocalDate.of(2025, 4, 8));
//        expense.setDescription("Compras mensuales");
//        expense.setUser(currentUser);
//        return expense;
//    }
//}