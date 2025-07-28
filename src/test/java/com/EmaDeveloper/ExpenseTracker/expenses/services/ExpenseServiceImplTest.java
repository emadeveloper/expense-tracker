package com.EmaDeveloper.ExpenseTracker.expenses.services;

import com.EmaDeveloper.ExpenseTracker.auth.services.AuthService;
import com.EmaDeveloper.ExpenseTracker.exceptions.custom.ExpenseNotFoundException;
import com.EmaDeveloper.ExpenseTracker.expenses.dto.ExpenseRequestDTO;
import com.EmaDeveloper.ExpenseTracker.expenses.dto.ExpenseResponseDTO;
import com.EmaDeveloper.ExpenseTracker.expenses.entities.Expense;
import com.EmaDeveloper.ExpenseTracker.expenses.repository.ExpenseRepository;
import com.EmaDeveloper.ExpenseTracker.users.entities.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ExpenseServiceImpl Tests")
class ExpenseServiceImplTest {
    @InjectMocks
    private ExpenseServiceImpl expenseService;

    @Mock
    private ExpenseRepository expenseRepository;

    @Mock
    private AuthService authService;

    // Instance variables initialized in @BeforeEach
    private User currentUser;
    private User anotherUser;
    private Expense validExpense;
    private Expense secondValidExpense;
    private ExpenseRequestDTO validExpenseRequestDTO;

    @BeforeEach
    void setUp() {
        currentUser = User.builder()
                .id(1L)
                .username("testUser")
                .email("test@example.com")
                .build();

        anotherUser = User.builder()
                .id(2L)
                .username("anotherUser")
                .email("test@example.com")
                .build();

        validExpense = Expense.builder()
                .id(1L)
                .title("Test Expense")
                .amount(40.0)
                .description("Restaurant bill")
                .category("Food")
                .date(LocalDate.of(2025, 5, 22))
                .user(currentUser)
                .build();

        secondValidExpense = Expense.builder()
                .id(2L)
                .title("Test Expense")
                .amount(60.0)
                .description("Grocery shopping")
                .category("Food")
                .date(LocalDate.of(2025, 5, 25))
                .user(currentUser)
                .build();

        validExpenseRequestDTO = new ExpenseRequestDTO();
        validExpenseRequestDTO.setTitle("Test Expense");
        validExpenseRequestDTO.setAmount(40.0);
        validExpenseRequestDTO.setDescription("Restaurant bill");
        validExpenseRequestDTO.setCategory("Food");
        validExpenseRequestDTO.setDate(LocalDate.of(2025, 5, 22));

        ExpenseResponseDTO validExpenseResponseDTO = new ExpenseResponseDTO();
        validExpenseResponseDTO.setTitle("Test Expense");
        validExpenseResponseDTO.setAmount(40.0);
        validExpenseResponseDTO.setDescription("Restaurant bill");
        validExpenseResponseDTO.setDescription("Food");
        validExpenseResponseDTO.setDate(LocalDate.of(2025, 5, 22));
    }

    @Nested
    @DisplayName("Get all Expenses Tests - ADMIN ONLY")
    class GetAllExpensesTests {

        @Test
        @DisplayName("Should return all expenses from all users - ADMIN functionality")
        void shouldReturnAllExpenses_AdminOnly() {
            // Arrange
            List<Expense> allExpenses = Arrays.asList(validExpense, secondValidExpense);
            when(expenseRepository.findAll()).thenReturn(allExpenses);

            // Act
            List<ExpenseResponseDTO> result = expenseService.getAllExpenses();

            // Assert
            assertNotNull(result);
            assertEquals(2, result.size());
            verify(expenseRepository, times(1)).findAll();
        }

        @Test
        @DisplayName("Should return empty list when no expenses found")
        void getAllExpenses_shouldReturnEmptyListWhenNoExpensesFound() {
            // Arrange
            when(expenseRepository.findAll()).thenReturn(Collections.emptyList());

            // Act
            List<ExpenseResponseDTO> result = expenseService.getAllExpenses();

            // Assert
            assertNotNull(result);
            assertTrue(result.isEmpty());
            verify(expenseRepository, times(1)).findAll();
        }

        @Test
        @DisplayName("Should handle Repository failure")
        void getAllExpenses_shouldHandleRepositoryFailure() {
            // Arrange
            when(expenseRepository.findAll()).thenThrow(new RuntimeException("Database error"));

            // Act & Assert
            assertThrows(RuntimeException.class, () -> expenseService.getAllExpenses());

            verify(expenseRepository, times(1)).findAll();
        }
    }

    @Nested
    @DisplayName("Get All Expenses By Current User")
    class getAllExpensesByCurrentUserTests {

        @Test
        @DisplayName("Should return all expenses for the current user")
        void getAllExpensesByCurrentUser_ShouldReturnAllExpensesForCurrentUser() {
            // Arrange
            List<Expense> userExpenses = Arrays.asList(validExpense, secondValidExpense);
            when(authService.getCurrentUser()).thenReturn(currentUser);
            when(expenseRepository.findAllByUserOrderByDateDesc(currentUser)).thenReturn(userExpenses);
            // Act
            List<ExpenseResponseDTO> result = expenseService.getAllExpensesByCurrentUser();

            // Assert
            assertNotNull(result);
            assertEquals(2, result.size());
            verify(expenseRepository, times(1)).findAllByUserOrderByDateDesc(currentUser);
        }

        @Test
        @DisplayName("Should return empty list when no expenses found for current user")
        void getAllExpensesByCurrentUser_shouldReturnEmptyListWhenNoExpensesFound() {
            // Arrange
            when(authService.getCurrentUser()).thenReturn(currentUser);
            when(expenseRepository.findAllByUserOrderByDateDesc(currentUser)).thenReturn(Collections.emptyList());

            // Act
            List<ExpenseResponseDTO> result = expenseService.getAllExpensesByCurrentUser();

            // Assert
            assertNotNull(result);
            assertTrue(result.isEmpty());
            verify(authService, times(1)).getCurrentUser();
            verify(expenseRepository, times(1)).findAllByUserOrderByDateDesc(currentUser);
        }

        @Test
        @DisplayName("Should throw exception UserNotFoundException when current user is null")
        void getAllExpensesByCurrentUser_shouldThrowExceptionWhenCurrentUserIsNull() {
            // Arrange
            when(authService.getCurrentUser()).thenThrow(new UsernameNotFoundException("User not found"));

            // Act & Assert
            assertThrows(UsernameNotFoundException.class, () -> expenseService.getAllExpensesByCurrentUser());

            verify(authService, times(1)).getCurrentUser();
            verify(expenseRepository, never()).findAllByUserOrderByDateDesc(any());
        }
    }

    @Nested
    @DisplayName("getExpenseById Tests")
    class getExpenseByIdTests {
        @Test
        @DisplayName("Should return expense when found and belongs to current user")
        void getExpenseById_ShouldReturnExpenseByIdWhenFoundAndBelongsToCurrentUser() {
            // Arrange
            Long expenseId = 1L;
            when(authService.getCurrentUser()).thenReturn(currentUser);
            when(expenseRepository.findById(expenseId)).thenReturn(Optional.of(validExpense));

            // Act
            ExpenseResponseDTO result = expenseService.getExpenseById(expenseId);

            // Assert
            assertNotNull(result);
            assertEquals(expenseId, result.getId());
            assertEquals("Test Expense", result.getTitle());
            assertEquals(40.0, result.getAmount());

            verify(authService, times(1)).getCurrentUser();
            verify(expenseRepository, times(1)).findById(expenseId);
        }

        @Test
        @DisplayName("Should throw exception when expense not found")
        void getExpenseById_ShouldThrowExpenseNotFoundException_WhenExpenseNotFound(){
            // Arrange
            Long expenseId = 1L;
            when(authService.getCurrentUser()).thenReturn(currentUser);
            when(expenseRepository.findById(expenseId)).thenReturn(Optional.empty());

            // Act & Assert
            ExpenseNotFoundException exception = assertThrows(ExpenseNotFoundException.class, 
                    () -> expenseService.getExpenseById(expenseId));

            assertEquals("Expense not found with id: " + expenseId, exception.getMessage());
            verify(authService, times(1)).getCurrentUser();
            verify(expenseRepository, times(1)).findById(expenseId);
        }

        @Test
        @DisplayName("Should throw exception when expense does not belong to current user")
        void getExpenseById_ShouldThrowResponseStatusException_WhenExpenseDoesNotBelongToCurrentUser() {
            // Arrange
            Long expenseId = 1L;

            Expense anotherUserExpense = Expense.builder()
                    .id(expenseId)
                    .title("Another User's Expense")
                    .amount(100.0)
                    .description("Some description")
                    .category("Other")
                    .date(LocalDate.of(2025, 5, 22))
                    .user(anotherUser)
                    .build();

            when(authService.getCurrentUser()).thenReturn(currentUser);
            when(expenseRepository.findById(expenseId)).thenReturn(Optional.of(anotherUserExpense));

            // Act & Assert
            ResponseStatusException exception = assertThrows(ResponseStatusException.class, 
                    () -> expenseService.getExpenseById(expenseId));

            assertEquals(HttpStatus.FORBIDDEN, exception.getStatusCode());
            assertEquals("You do not have permission to access this expense", exception.getReason());

            verify(authService, times(1)).getCurrentUser();
            verify(expenseRepository, times(1)).findById(expenseId);
        }
    }

    @Nested
    @DisplayName("postExpense Tests")
    class postExpenseTests {
        @Test
        @DisplayName("Should create and return a new expense")
        void postExpense_ShouldCreateAndReturnNewExpense() {
            // Arrange
            when(authService.getCurrentUser()).thenReturn(currentUser);
            when(expenseRepository.save(any(Expense.class))).thenReturn(validExpense);

            // Act
            ExpenseResponseDTO result = expenseService.postExpense(validExpenseRequestDTO);

            // Assert
            assertNotNull(result);
            assertEquals("Test Expense", result.getTitle());
            assertEquals(40.0, result.getAmount());
            assertEquals("Restaurant bill", result.getDescription());
            assertEquals("Food", result.getCategory());

            verify(authService, times(1)).getCurrentUser();
            verify(expenseRepository, times(1)).save(any(Expense.class));
        }

        @Test
        @DisplayName("Should throw UsernameNotFoundException when current user not found in database")
        void postExpense_ShouldThrowUsernameNotFoundException_WhenCurrentUserNotFound() {
            // Arrange
            when(authService.getCurrentUser()).thenThrow(new UsernameNotFoundException("User not found"));

            // Act & Assert
            UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, 
                    () -> expenseService.postExpense(validExpenseRequestDTO));

            assertEquals("User not found", exception.getMessage());
            verify(authService, times(1)).getCurrentUser();
            verify(expenseRepository, never()).save(any(Expense.class));
        }

        @Test
        @DisplayName("Should handle Repository failure when saving expense")
        void postExpense_ShouldHandleRespositoryFailure_WhenSavingExpense() {
            // Arrange
            when(authService.getCurrentUser()).thenReturn(currentUser);
            when(expenseRepository.save(any(Expense.class)))
                    .thenThrow(new RuntimeException("Database error"));

            // Act & Assert
            RuntimeException exception = assertThrows(RuntimeException.class, 
                    () -> expenseService.postExpense(validExpenseRequestDTO));

            assertEquals("Database error", exception.getMessage());
            verify(authService, times(1)).getCurrentUser();
            verify(expenseRepository, times(1)).save(any(Expense.class));
        }

        @Test
        @DisplayName("Should create expense with minimum required fields")
        void postExpense_ShouldCreateExpenseWithMinimumRequiredFields() {
            // Arrange
            Long expenseId = validExpense.getId();
            ExpenseRequestDTO updateDTO = new ExpenseRequestDTO();
            updateDTO.setTitle("Updated Expense");
            updateDTO.setAmount(6000.0);
            updateDTO.setCategory("Updated Category");
            updateDTO.setDate(LocalDate.of(2025, 5, 10));
            updateDTO.setDescription("Updated expense description");

            // Income mock for update
            Expense updatedExpense = Expense.builder()
                    .id(expenseId)
                    .title("Updated Expense")
                    .amount(6000.0)
                    .category("Updated Category")
                    .date(LocalDate.of(2025, 5, 10))
                    .description("Updated expense description")
                    .user(currentUser)
                    .build();

            when(authService.getCurrentUser()).thenReturn(currentUser);
            when(expenseRepository.findById(expenseId)).thenReturn(Optional.of(validExpense));
            when(expenseRepository.save(any(Expense.class))).thenReturn(updatedExpense);

            // Act
            ExpenseResponseDTO result = expenseService.updateExpense(expenseId, updateDTO);

            // Assert
            assertNotNull(result);
            assertEquals("Updated Expense", result.getTitle());
            assertEquals(6000.0, result.getAmount());
            assertEquals("Updated Category", result.getCategory());

            verify(authService, times(1)).getCurrentUser();
            verify(expenseRepository, times(1)).findById(expenseId);
            verify(expenseRepository, times(1)).save(any(Expense.class));
        }
    }

    @Nested
    @DisplayName("updateExpense Tests")
    class updateExpenseTests {
        @Test
        @DisplayName("Should update and return an existing expense")
        void updateExpense_ShouldUpdateAndReturnExistingExpense() {
            // Arrange
            Long expenseId = 1L;
            ExpenseRequestDTO updateRequest = new ExpenseRequestDTO();
            updateRequest.setTitle("Updated Expense");
            updateRequest.setAmount(50.0);
            updateRequest.setDescription("Updated description");
            updateRequest.setCategory("Updated Category");
            updateRequest.setDate(LocalDate.of(2025, 5, 23));

            when(authService.getCurrentUser()).thenReturn(currentUser);
            when(expenseRepository.findById(expenseId)).thenReturn(Optional.of(validExpense));
            when(expenseRepository.save(any(Expense.class))).thenReturn(validExpense);

            // Act
            ExpenseResponseDTO result = expenseService.updateExpense(expenseId, updateRequest);

            // Assert
            assertNotNull(result);
            assertEquals("Updated Expense", result.getTitle());
            assertEquals(50.0, result.getAmount());
            assertEquals("Updated description", result.getDescription());
            assertEquals("Updated Category", result.getCategory());
            assertEquals(LocalDate.of(2025, 5, 23), result.getDate());

            verify(authService, times(1)).getCurrentUser();
            verify(expenseRepository, times(1)).findById(expenseId);
            verify(expenseRepository, times(1)).save(any(Expense.class));
        }

        @Test
        @DisplayName("Should throw ExpenseNotFoundException when expense does not exist")
        void updateExpense_ShouldThrowExpenseNotFoundException_WhenExpenseDoesNotExist() {
            // Arrange
            Long expenseId = 999L; // Non-existent ID
            ExpenseRequestDTO updateRequest = new ExpenseRequestDTO();
            updateRequest.setTitle("Updated Expense");
            updateRequest.setAmount(50.0);
            updateRequest.setDescription("Updated description");
            updateRequest.setCategory("Updated Category");
            updateRequest.setDate(LocalDate.of(2025, 5, 23));

            when(authService.getCurrentUser()).thenReturn(currentUser);
            when(expenseRepository.findById(expenseId)).thenReturn(Optional.empty());

            // Act & Assert
            ExpenseNotFoundException exception = assertThrows(ExpenseNotFoundException.class, 
                    () -> expenseService.updateExpense(expenseId, updateRequest));

            assertEquals("Expense not found with id: " + expenseId, exception.getMessage());
            verify(authService, times(1)).getCurrentUser();
            verify(expenseRepository, times(1)).findById(expenseId);
            verify(expenseRepository, never()).save(any(Expense.class));
        }

        @Test
        @DisplayName("Should throw ResponseStatusException when user doesn't own expense")
        void updateExpense_ShouldThrowResponseStatusException_WhenUserDoesNotOwnExpense() {
            // Arrange
            Long expenseId = 1L;

            Expense anotherUserExpense = Expense.builder()
                    .id(expenseId)
                    .user(anotherUser)
                    .build();

            when(authService.getCurrentUser()).thenReturn(currentUser);
            when(expenseRepository.findById(expenseId)).thenReturn(Optional.of(anotherUserExpense));

            // Act & Assert
            ResponseStatusException exception = assertThrows(ResponseStatusException.class, 
                    () -> expenseService.updateExpense(expenseId, validExpenseRequestDTO));

            assertEquals(HttpStatus.FORBIDDEN, exception.getStatusCode());
            assertEquals("You do not have permission to update this expense", exception.getReason());

            verify(authService, times(1)).getCurrentUser();
            verify(expenseRepository, times(1)).findById(expenseId);
            verify(expenseRepository, never()).save(any(Expense.class));
        }

        @Test
        @DisplayName("Should throw UsernameNotFoundException when current user not found in database")
        void updateExpense_shouldThrowUsernameNotFoundException_whenUserNotFoundInDB() {
            // Arrange
            Long expenseId = 1L;
            when(authService.getCurrentUser()).thenThrow(new UsernameNotFoundException("User not found"));

            // Act & Assert
            assertThrows(UsernameNotFoundException.class, 
                    () -> expenseService.updateExpense(expenseId, validExpenseRequestDTO));

            verify(authService, times(1)).getCurrentUser();
            verify(expenseRepository, never()).findById(expenseId);
            verify(expenseRepository, never()).save(any(Expense.class));
        }
    }

    @Nested
    @DisplayName("deleteExpense Tests")
    class DeleteIncomeTests {
        @Test
        @DisplayName("Should delete an existing expense when it belongs to the current user")
        void deleteExpense_ShouldDeleteExistingExpense_WhenItBelongsToTheCurrentUser() {
            // Arrange
            Long expenseId = validExpense.getId();
            when(authService.getCurrentUser()).thenReturn(currentUser);
            when(expenseRepository.existsById(expenseId)).thenReturn(true);
            when(expenseRepository.findById(expenseId)).thenReturn(Optional.of(validExpense));

            // Act
            expenseService.deleteExpense(expenseId);

            // Assert
            verify(authService, times(1)).getCurrentUser();
            verify(expenseRepository, times(1)).existsById(expenseId);
            verify(expenseRepository, times(1)).findById(expenseId);
            verify(expenseRepository, times(1)).deleteById(expenseId);
        }

        @Test
        @DisplayName("Should throw ExpenseNotFoundException when expense doesn't exist")
        void deleteExpense_ShouldThrowExpenseNotFoundException_WhenExpenseDoesNotExist() {
            Long expenseId = 999L;
            when(expenseRepository.existsById(expenseId)).thenReturn(false);

            // Act & Assert
            ExpenseNotFoundException exception = assertThrows(ExpenseNotFoundException.class, 
                    () -> expenseService.deleteExpense(expenseId));

            assertEquals(expenseId, exception.getId());
            verify(expenseRepository, times(1)).existsById(expenseId);
            verify(authService, never()).getCurrentUser();
            verify(expenseRepository, never()).findById(expenseId);
            verify(expenseRepository, never()).deleteById(expenseId);
        }

        @Test
        @DisplayName("Should throw SecurityException when user doesn't own income")
        void deleteIncome_shouldThrowSecurityException_whenUserDoesNotOwnIncome() {
            // Arrange
            Long expenseId = 1L;
            Expense otherUserExpense = Expense.builder()
                    .id(expenseId)
                    .user(anotherUser)
                    .build();

            when(expenseRepository.existsById(expenseId)).thenReturn(true);
            when(authService.getCurrentUser()).thenReturn(currentUser);
            when(expenseRepository.findById(expenseId)).thenReturn(Optional.of(otherUserExpense));

            // Act & Assert
            SecurityException exception = assertThrows(SecurityException.class, 
                    () -> expenseService.deleteExpense(expenseId));

            assertEquals("You do not have permission to delete this expense", exception.getMessage());
            verify(expenseRepository, times(1)).existsById(expenseId);
            verify(authService, times(1)).getCurrentUser();
            verify(expenseRepository, times(1)).findById(expenseId);
            verify(expenseRepository, never()).deleteById(expenseId);
        }

        @Test
        @DisplayName("Should handle empty optional when expense not found after exists check")
        void deleteExpense_ShouldHandleEmptyOptional_WhenExpenseNotFoundAfterExistsCheck() {
            // Arrange
            Long expenseId = 1L;
            when(authService.getCurrentUser()).thenReturn(currentUser);
            when(expenseRepository.existsById(expenseId)).thenReturn(true);
            when(expenseRepository.findById(expenseId)).thenReturn(Optional.empty());

            // Act
            expenseService.deleteExpense(expenseId);

            // Assert
            verify(authService, times(1)).getCurrentUser();
            verify(expenseRepository, times(1)).existsById(expenseId);
            verify(expenseRepository, times(1)).findById(expenseId);
        }

        @Test
        @DisplayName("Should throw UsernameNotFoundException when current user not found in database")
        void deleteExpense_ShouldThrowUsernameNotFoundException_WhenCurrentUserNotFound() {
            // Arrange
            Long expenseId = 1L;
            when(expenseRepository.existsById(expenseId)).thenReturn(true);
            when(authService.getCurrentUser()).thenThrow(new UsernameNotFoundException("User not found"));

            // Act & Assert
            assertThrows(UsernameNotFoundException.class, 
                    () -> expenseService.deleteExpense(expenseId));

            verify(expenseRepository, times(1)).existsById(expenseId);
            verify(authService, times(1)).getCurrentUser();
            verify(expenseRepository, never()).deleteById(expenseId);
        }
    }
}
