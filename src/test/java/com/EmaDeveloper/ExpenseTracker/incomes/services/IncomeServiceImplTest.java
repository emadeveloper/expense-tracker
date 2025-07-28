package com.EmaDeveloper.ExpenseTracker.incomes.services;

import com.EmaDeveloper.ExpenseTracker.auth.services.AuthService;
import com.EmaDeveloper.ExpenseTracker.exceptions.custom.IncomeNotFoundException;
import com.EmaDeveloper.ExpenseTracker.incomes.dto.IncomeRequestDTO;
import com.EmaDeveloper.ExpenseTracker.incomes.dto.IncomeResponseDTO;
import com.EmaDeveloper.ExpenseTracker.incomes.entities.Income;
import com.EmaDeveloper.ExpenseTracker.incomes.respository.IncomeRepository;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("IncomeService Tests")
class IncomeServiceImplTest {

    @Mock
    private IncomeRepository incomeRepository;

    @Mock
    private AuthService authService;

    @InjectMocks
    private IncomeServiceImpl incomeService;

    // Instance variables initialized in @BeforeEach
    private User currentUser;
    private User otherUser;
    private Income validIncome;
    private Income secondIncome;
    private IncomeRequestDTO validIncomeDTO;

    @BeforeEach
    void setUp() {
        // Current user setup for tests
        currentUser = User.builder()
                .id(1L)
                .username("testUser")
                .email("test@example.com")
                .build();

        // Other user setup for security tests
        otherUser = User.builder()
                .id(2L)
                .username("otherUser")
                .email("other@example.com")
                .build();

        // Valid income setup for tests
        validIncome = Income.builder()
                .id(1L)
                .title("Salary")
                .amount(5000.0)
                .description("Monthly salary payment")
                .category("Job")
                .date(LocalDate.of(2025, 4, 10))
                .user(currentUser)
                .build();

        // Second income setup for listing tests
        secondIncome = Income.builder()
                .id(2L)
                .title("Freelance")
                .amount(2000.0)
                .description("Web development project")
                .category("Freelance")
                .date(LocalDate.of(2025, 4, 15))
                .user(currentUser)
                .build();

        // Valid Income Request DTO setup
        validIncomeDTO = new IncomeRequestDTO();
        validIncomeDTO.setTitle("Salary");
        validIncomeDTO.setAmount(5000.0);
        validIncomeDTO.setCategory("Job");
        validIncomeDTO.setDate(LocalDate.of(2025, 4, 10));
        validIncomeDTO.setDescription("Monthly salary payment");

        // Other valid Response DTO setup
        IncomeResponseDTO validIncomeResponseDTO = new IncomeResponseDTO();
        validIncomeResponseDTO.setId(1L);
        validIncomeResponseDTO.setTitle("Salary");
        validIncomeResponseDTO.setAmount(5000.0);
        validIncomeResponseDTO.setCategory("Job");
        validIncomeResponseDTO.setDate(LocalDate.of(2025, 4, 10));
        validIncomeResponseDTO.setDescription("Monthly salary payment");
    }

    @Nested
    @DisplayName("getAllIncomes Tests - ADMIN Only")
    class GetAllIncomesTests {

        @Test
        @DisplayName("Should return all incomes from all users - ADMIN functionality")
        void getAllIncomes_shouldReturnAllIncomes_adminOnly() {
            // Arrange
            List<Income> allIncomes = Arrays.asList(validIncome, secondIncome);
            when(incomeRepository.findAll()).thenReturn(allIncomes);

            // Act
            List<IncomeResponseDTO> result = incomeService.getAllIncomes();

            // Assert
            assertNotNull(result);
            assertEquals(2, result.size());
            verify(incomeRepository, times(1)).findAll();
        }

        @Test
        @DisplayName("Should return empty list when no incomes exist")
        void getAllIncomes_shouldReturnEmptyList_whenNoIncomesExist() {
            // Arrange
            when(incomeRepository.findAll()).thenReturn(Collections.emptyList());

            // Act
            List<IncomeResponseDTO> result = incomeService.getAllIncomes();

            // Assert
            assertNotNull(result);
            assertTrue(result.isEmpty());
            verify(incomeRepository, times(1)).findAll();
        }

        @Test
        @DisplayName("Should handle repository failure")
        void getAllIncomes_shouldThrowException_whenRepositoryFails() {
            // Arrange
            when(incomeRepository.findAll()).thenThrow(new RuntimeException("Database error"));

            // Act & Assert
            assertThrows(RuntimeException.class, () -> {
                incomeService.getAllIncomes();
            });

            verify(incomeRepository, times(1)).findAll();
        }
    }

    @Nested
    @DisplayName("Get All Incomes By Current User Tests")
    class GetAllIncomesByCurrentUserTests {

        @Test
        @DisplayName("Should return current user's incomes sorted by date desc")
        void getAllIncomesByCurrentUser_shouldReturnUserIncomes_sortedByDateDesc() {
            // Arrange
            List<Income> userIncomes = Arrays.asList(secondIncome, validIncome);
            when(authService.getCurrentUser()).thenReturn(currentUser);
            when(incomeRepository.findAllByUserOrderByDateDesc(currentUser)).thenReturn(userIncomes);

            // Act
            List<IncomeResponseDTO> result = incomeService.getAllIncomesByCurrentUser();

            // Assert
            assertNotNull(result);
            assertEquals(2, result.size());
            // Check that the first income is after the second one
            assertTrue(result.get(0).getDate().isAfter(result.get(1).getDate()));

            verify(authService, times(1)).getCurrentUser();
            verify(incomeRepository, times(1)).findAllByUserOrderByDateDesc(currentUser);
        }

        @Test
        @DisplayName("Should return empty list when user has no incomes")
        void getAllIncomesByCurrentUser_shouldReturnEmptyList_whenUserHasNoIncomes() {
            // Arrange
            when(authService.getCurrentUser()).thenReturn(currentUser);
            when(incomeRepository.findAllByUserOrderByDateDesc(currentUser)).thenReturn(Collections.emptyList());

            // Act
            List<IncomeResponseDTO> result = incomeService.getAllIncomesByCurrentUser();

            // Assert
            assertNotNull(result);
            assertTrue(result.isEmpty());

            verify(authService, times(1)).getCurrentUser();
            verify(incomeRepository, times(1)).findAllByUserOrderByDateDesc(currentUser);
        }

        @Test
        @DisplayName("Should throw UsernameNotFoundException when current user not found in database")
        void getAllIncomesByCurrentUser_shouldThrowUsernameNotFoundException_whenUserNotFoundInDB() {
            // Arrange
            when(authService.getCurrentUser()).thenThrow(new UsernameNotFoundException("User not found"));

            // Act & Assert
            assertThrows(UsernameNotFoundException.class, () -> {
                incomeService.getAllIncomesByCurrentUser();
            });

            verify(authService, times(1)).getCurrentUser();
            verify(incomeRepository, never()).findAllByUserOrderByDateDesc(any());
        }
    }

    @Nested
    @DisplayName("getIncomeById Tests")
    class GetIncomeByIdTests {

        @Test
        @DisplayName("Should return income when found and user owns it")
        void getIncomeById_shouldReturnIncome_whenFoundAndUserOwnsIt() {
            // Arrange
            Long incomeId = validIncome.getId();
            when(authService.getCurrentUser()).thenReturn(currentUser);
            when(incomeRepository.findById(incomeId)).thenReturn(Optional.of(validIncome));

            // Act
            IncomeResponseDTO result = incomeService.getIncomeById(incomeId);

            // Assert
            assertNotNull(result);
            assertEquals(incomeId, result.getId());
            assertEquals("Salary", result.getTitle());
            assertEquals(5000.0, result.getAmount());

            verify(authService, times(1)).getCurrentUser();
            verify(incomeRepository, times(1)).findById(incomeId);
        }

        @Test
        @DisplayName("Should throw IncomeNotFoundException when income not found")
        void getIncomeById_shouldThrowIncomeNotFoundException_whenNotFound() {
            // Arrange
            Long incomeId = 999L;
            when(authService.getCurrentUser()).thenReturn(currentUser);
            when(incomeRepository.findById(incomeId)).thenReturn(Optional.empty());

            // Act & Assert
            IncomeNotFoundException exception = assertThrows(IncomeNotFoundException.class, () -> {
                incomeService.getIncomeById(incomeId);
            });

            assertEquals("Income not found with id: " + incomeId, exception.getMessage());
            verify(authService, times(1)).getCurrentUser();
            verify(incomeRepository, times(1)).findById(incomeId);
        }

        @Test
        @DisplayName("Should throw ResponseStatusException when user doesn't own income")
        void getIncomeById_shouldThrowResponseStatusException_whenUserDoesNotOwnIncome() {
            // Arrange
            Long incomeId = 1L;
            Income otherUserIncome = Income.builder()
                    .id(incomeId)
                    .title("Other's income")
                    .user(otherUser)
                    .build();

            when(authService.getCurrentUser()).thenReturn(currentUser);
            when(incomeRepository.findById(incomeId)).thenReturn(Optional.of(otherUserIncome));

            // Act & Assert
            ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
                incomeService.getIncomeById(incomeId);
            });

            assertEquals(HttpStatus.FORBIDDEN, exception.getStatusCode());
            assertEquals("You do not have permission to access this income", exception.getReason());

            verify(authService, times(1)).getCurrentUser();
            verify(incomeRepository, times(1)).findById(incomeId);
        }
    }

    @Nested
    @DisplayName("postIncome Tests")
    class PostIncomeTests {

        @Test
        @DisplayName("Should create income successfully with valid data")
        void postIncome_shouldCreateIncome_whenValidData() {
            // Arrange
            when(authService.getCurrentUser()).thenReturn(currentUser);
            when(incomeRepository.save(any(Income.class))).thenReturn(validIncome);

            // Act
            IncomeResponseDTO result = incomeService.postIncome(validIncomeDTO);

            // Assert
            assertNotNull(result);
            assertEquals("Salary", result.getTitle());
            assertEquals(5000.0, result.getAmount());
            assertEquals("Job", result.getCategory());

            verify(authService, times(1)).getCurrentUser();
            verify(incomeRepository, times(1)).save(any(Income.class));
        }

        @Test
        @DisplayName("Should throw UsernameNotFoundException when current user not found in database")
        void postIncome_shouldThrowUsernameNotFoundException_whenUserNotFoundInDB() {
            // Arrange
            when(authService.getCurrentUser()).thenThrow(new UsernameNotFoundException("User not found"));

            // Act & Assert
            assertThrows(UsernameNotFoundException.class, () -> {
                incomeService.postIncome(validIncomeDTO);
            });

            verify(authService, times(1)).getCurrentUser();
            verify(incomeRepository, never()).save(any(Income.class));
        }

        @Test
        @DisplayName("Should handle repository save failure")
        void postIncome_shouldThrowException_whenRepositoryFails() {
            // Arrange
            when(authService.getCurrentUser()).thenReturn(currentUser);
            when(incomeRepository.save(any(Income.class)))
                    .thenThrow(new RuntimeException("Database error"));

            // Act & Assert
            assertThrows(RuntimeException.class, () -> {
                incomeService.postIncome(validIncomeDTO);
            });

            verify(authService, times(1)).getCurrentUser();
            verify(incomeRepository, times(1)).save(any(Income.class));
        }

        @Test
        @DisplayName("Should create income with minimum required fields")
        void postIncome_shouldCreateIncome_withMinimumFields() {
            // Arrange
            IncomeRequestDTO minimalDTO = new IncomeRequestDTO();
            minimalDTO.setTitle("Test");
            minimalDTO.setAmount(100.0);
            minimalDTO.setCategory("Test");
            minimalDTO.setDate(LocalDate.now());

            Income minimalIncome = Income.builder()
                    .id(1L)
                    .title("Test")
                    .amount(100.0)
                    .category("Test")
                    .date(LocalDate.now())
                    .user(currentUser)
                    .build();

            when(authService.getCurrentUser()).thenReturn(currentUser);
            when(incomeRepository.save(any(Income.class))).thenReturn(minimalIncome);

            // Act
            IncomeResponseDTO result = incomeService.postIncome(minimalDTO);

            // Assert
            assertNotNull(result);
            assertEquals("Test", result.getTitle());
            assertEquals(100.0, result.getAmount());
        }
    }

    @Nested
    @DisplayName("updateIncome Tests")
    class UpdateIncomeTests {

        @Test
        @DisplayName("Should update income successfully when user owns it")
        void updateIncome_shouldUpdateIncome_whenUserOwnsIt() {
            // Arrange
            Long incomeId = validIncome.getId();
            IncomeRequestDTO updateDTO = new IncomeRequestDTO();
            updateDTO.setTitle("Updated Salary");
            updateDTO.setAmount(6000.0);
            updateDTO.setCategory("Updated Job");
            updateDTO.setDate(LocalDate.of(2025, 5, 10));
            updateDTO.setDescription("Updated salary payment");

            // Income mock for update
            Income updatedIncome = Income.builder()
                    .id(incomeId)
                    .title("Updated Salary")
                    .amount(6000.0)
                    .category("Updated Job")
                    .date(LocalDate.of(2025, 5, 10))
                    .description("Updated salary payment")
                    .user(currentUser)
                    .build();

            when(authService.getCurrentUser()).thenReturn(currentUser);
            when(incomeRepository.findById(incomeId)).thenReturn(Optional.of(validIncome));
            when(incomeRepository.save(any(Income.class))).thenReturn(updatedIncome);

            // Act
            IncomeResponseDTO result = incomeService.updateIncome(incomeId, updateDTO);

            // Assert
            assertNotNull(result);
            assertEquals("Updated Salary", result.getTitle());
            assertEquals(6000.0, result.getAmount());
            assertEquals("Updated Job", result.getCategory());

            verify(authService, times(1)).getCurrentUser();
            verify(incomeRepository, times(1)).findById(incomeId);
            verify(incomeRepository, times(1)).save(any(Income.class));
        }

        @Test
        @DisplayName("Should throw IncomeNotFoundException when income not found")
        void updateIncome_shouldThrowIncomeNotFoundException_whenNotFound() {
            // Arrange
            Long incomeId = 999L;
            when(incomeRepository.findById(incomeId)).thenReturn(Optional.empty());

            // Act & Assert
            IncomeNotFoundException exception = assertThrows(IncomeNotFoundException.class, () -> {
                incomeService.updateIncome(incomeId, validIncomeDTO);
            });

            assertEquals("Income not found with id: " + incomeId, exception.getMessage());
            verify(incomeRepository, times(1)).findById(incomeId);
            verify(incomeRepository, never()).save(any(Income.class));
        }

        @Test
        @DisplayName("Should throw ResponseStatusException when user doesn't own income")
        void updateIncome_shouldThrowResponseStatusException_whenUserDoesNotOwnIncome() {
            // Arrange
            Long incomeId = 1L;
            Income otherUserIncome = Income.builder()
                    .id(incomeId)
                    .user(otherUser)
                    .build();

            when(authService.getCurrentUser()).thenReturn(currentUser);
            when(incomeRepository.findById(incomeId)).thenReturn(Optional.of(otherUserIncome));

            // Act & Assert
            ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
                incomeService.updateIncome(incomeId, validIncomeDTO);
            });

            assertEquals(HttpStatus.FORBIDDEN, exception.getStatusCode());
            assertEquals("You do not have permission to update this income", exception.getReason());

            verify(authService, times(1)).getCurrentUser();
            verify(incomeRepository, times(1)).findById(incomeId);
            verify(incomeRepository, never()).save(any(Income.class));
        }

        @Test
        @DisplayName("Should throw UsernameNotFoundException when current user not found in database")
        void updateIncome_shouldThrowUsernameNotFoundException_whenUserNotFoundInDB() {
            // Arrange
            Long incomeId = 1L;
            when(authService.getCurrentUser()).thenThrow(new UsernameNotFoundException("User not found"));

            // Act & Assert
            assertThrows(UsernameNotFoundException.class, () -> {
                incomeService.updateIncome(incomeId, validIncomeDTO);
            });

            verify(authService, times(1)).getCurrentUser();
            verify(incomeRepository, never()).findById(incomeId);
            verify(incomeRepository, never()).save(any(Income.class));
        }
    }

    @Nested
    @DisplayName("deleteIncome Tests")
    class DeleteIncomeTests {

        @Test
        @DisplayName("Should delete income successfully when user owns it")
        void deleteIncome_shouldDeleteIncome_whenUserOwnsIt() {
            // Arrange
            Long incomeId = validIncome.getId();
            when(incomeRepository.existsById(incomeId)).thenReturn(true);
            when(authService.getCurrentUser()).thenReturn(currentUser);
            when(incomeRepository.findById(incomeId)).thenReturn(Optional.of(validIncome));

            // Act
            incomeService.deleteIncome(incomeId);

            // Assert
            verify(incomeRepository, times(1)).existsById(incomeId);
            verify(authService, times(1)).getCurrentUser();
            verify(incomeRepository, times(1)).findById(incomeId);
            verify(incomeRepository, times(1)).deleteById(incomeId);
        }

        @Test
        @DisplayName("Should throw IncomeNotFoundException when income doesn't exist")
        void deleteIncome_shouldThrowIncomeNotFoundException_whenIncomeDoesNotExist() {
            // Arrange
            Long incomeId = 999L;
            when(incomeRepository.existsById(incomeId)).thenReturn(false);

            // Act & Assert
            IncomeNotFoundException exception = assertThrows(IncomeNotFoundException.class, () -> {
                incomeService.deleteIncome(incomeId);
            });

            assertEquals(incomeId, exception.getId());
            verify(incomeRepository, times(1)).existsById(incomeId);
            verify(authService, never()).getCurrentUser();
            verify(incomeRepository, never()).findById(incomeId);
            verify(incomeRepository, never()).deleteById(incomeId);
        }

        @Test
        @DisplayName("Should throw SecurityException when user doesn't own income")
        void deleteIncome_shouldThrowSecurityException_whenUserDoesNotOwnIncome() {
            // Arrange
            Long incomeId = 1L;
            Income otherUserIncome = Income.builder()
                    .id(incomeId)
                    .user(otherUser)
                    .build();

            when(incomeRepository.existsById(incomeId)).thenReturn(true);
            when(authService.getCurrentUser()).thenReturn(currentUser);
            when(incomeRepository.findById(incomeId)).thenReturn(Optional.of(otherUserIncome));

            // Act & Assert
            SecurityException exception = assertThrows(SecurityException.class, () -> {
                incomeService.deleteIncome(incomeId);
            });

            assertEquals("You do not have permission to delete this expense", exception.getMessage());
            verify(incomeRepository, times(1)).existsById(incomeId);
            verify(authService, times(1)).getCurrentUser();
            verify(incomeRepository, times(1)).findById(incomeId);
            verify(incomeRepository, never()).deleteById(incomeId);
        }

        @Test
        @DisplayName("Should handle empty optional when income not found after exists check")
        void deleteIncome_shouldHandleEmptyOptional_whenIncomeNotFoundAfterExistsCheck() {
            // Arrange - Simulates race condition
            Long incomeId = 1L;
            when(incomeRepository.existsById(incomeId)).thenReturn(true);
            when(authService.getCurrentUser()).thenReturn(currentUser);
            when(incomeRepository.findById(incomeId)).thenReturn(Optional.empty());

            // Act
            incomeService.deleteIncome(incomeId);

            // Assert - must proceed to delete (idempotent operation)
            verify(incomeRepository, times(1)).deleteById(incomeId);
        }

        @Test
        @DisplayName("Should throw UsernameNotFoundException when current user not found in database")
        void deleteIncome_shouldThrowUsernameNotFoundException_whenUserNotFoundInDB() {
            // Arrange
            Long incomeId = 1L;
            when(incomeRepository.existsById(incomeId)).thenReturn(true);
            when(authService.getCurrentUser()).thenThrow(new UsernameNotFoundException("User not found"));

            // Act & Assert
            assertThrows(UsernameNotFoundException.class, () -> {
                incomeService.deleteIncome(incomeId);
            });

            verify(incomeRepository, times(1)).existsById(incomeId);
            verify(authService, times(1)).getCurrentUser();
            verify(incomeRepository, never()).deleteById(incomeId);
        }
    }
}