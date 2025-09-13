package com.EmaDeveloper.ExpenseTracker.expenses.services;

import com.EmaDeveloper.ExpenseTracker.exceptions.custom.ExpenseNotFoundException;
import com.EmaDeveloper.ExpenseTracker.expenses.dto.ExpenseRequestDTO;
import com.EmaDeveloper.ExpenseTracker.expenses.dto.ExpenseResponseDTO;
import com.EmaDeveloper.ExpenseTracker.expenses.entities.Expense;
import com.EmaDeveloper.ExpenseTracker.expenses.mapper.ExpenseMapper;
import com.EmaDeveloper.ExpenseTracker.users.entities.User;
import com.EmaDeveloper.ExpenseTracker.expenses.repository.ExpenseRepository;
import com.EmaDeveloper.ExpenseTracker.users.repository.UserRepository;
import com.EmaDeveloper.ExpenseTracker.auth.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ExpenseServiceImpl implements ExpenseService {
    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;
    private final AuthService authService;

    // method to get all expenses
    @Override
    public List<ExpenseResponseDTO> getAllExpenses() {
        return expenseRepository.findAll()
                .stream()
                .map(ExpenseMapper::toResponseDTO)
                .toList();
    }

    // method to get all expenses by the current user
    @Override
    public List<ExpenseResponseDTO> getAllExpensesByCurrentUser() {
        User user = authService.getCurrentUser();

        return expenseRepository.findAllByUserOrderByDateDesc(user)
                .stream()
                .map(ExpenseMapper::toResponseDTO)
                .toList();

    }

    // method to get the last 5 expenses by the current user
    @Override
    public List<ExpenseResponseDTO> getLast5ExpensesByCurrentUser() {
        User user = authService.getCurrentUser();

        return expenseRepository.findLast5ByUserOrderByDateDesc(user)
                .stream()
                .map(ExpenseMapper::toResponseDTO)
                .toList();
    }

    // method to get an expense by id
    @Override
    public ExpenseResponseDTO getExpenseById(Long id) {
        User currentUser = authService.getCurrentUser();

        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new ExpenseNotFoundException(id));

        if (!expense.getUser().getId().equals(currentUser.getId())){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have permission to access this expense");
        }

        return ExpenseMapper.toResponseDTO(expense);
    }

    // method to post a new expense
    @Override
    public ExpenseResponseDTO postExpense(ExpenseRequestDTO expenseDTO) {
        User user = authService.getCurrentUser();
        Expense expense = ExpenseMapper.toEntity(expenseDTO, user);

        Expense savedExpense = expenseRepository.save(expense);

        return ExpenseMapper.toResponseDTO(savedExpense);
    }

    // method to update an expense
    @Override
    public ExpenseResponseDTO updateExpense(Long id, ExpenseRequestDTO expenseDTO) {
        User currentUser = authService.getCurrentUser();

        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new ExpenseNotFoundException(id));

        if (!expense.getUser().getId().equals(currentUser.getId())){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have permission to update this expense");
        }
        expense.setTitle(expenseDTO.getTitle());
        expense.setDescription(expenseDTO.getDescription());
        expense.setDate(expenseDTO.getDate());
        expense.setCategory(expenseDTO.getCategory());
        expense.setAmount(expenseDTO.getAmount());

        Expense updatedExpense = expenseRepository.save(expense);

        return ExpenseMapper.toResponseDTO(updatedExpense);
    }

    // Method to delete an expense
    @Override
    public void deleteExpense(Long id) {
        if (!expenseRepository.existsById(id)) {
            throw new ExpenseNotFoundException(id);
        }
        // check if the expense belongs to the current user
        User currentUser = authService.getCurrentUser();
        Optional<Expense> expenseOptional = expenseRepository.findById(id);

        if (expenseOptional.isPresent() && !expenseOptional.get().getUser().equals(currentUser)) {
            throw new SecurityException("You do not have permission to delete this expense");
        }
         // Delete the expense
     expenseRepository.deleteById(id);
    }
}