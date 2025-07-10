package com.EmaDeveloper.ExpenseTracker.expenses.services;

import com.EmaDeveloper.ExpenseTracker.expenses.dto.ExpenseRequestDTO;
import com.EmaDeveloper.ExpenseTracker.expenses.dto.ExpenseResponseDTO;
import com.EmaDeveloper.ExpenseTracker.expenses.entities.Expense;
import com.EmaDeveloper.ExpenseTracker.expenses.mapper.ExpenseMapper;
import com.EmaDeveloper.ExpenseTracker.users.entities.User;
import com.EmaDeveloper.ExpenseTracker.expenses.repository.ExpenseRepository;
import com.EmaDeveloper.ExpenseTracker.users.repository.UserRepository;
import com.EmaDeveloper.ExpenseTracker.auth.services.AuthService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ExpenseServiceImpl implements ExpenseService {
    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;
    private final AuthService authService;

    // method to save or update an expense
    private Expense saveOrUpdateExpense(Expense expense, ExpenseRequestDTO expenseDTO) {
        expense.setId(expense.getId());
        expense.setTitle(expenseDTO.getTitle());
        expense.setDate(expenseDTO.getDate());
        expense.setCategory(expenseDTO.getCategory());
        expense.setAmount(expenseDTO.getAmount());
        expense.setDescription(expenseDTO.getDescription());

        expense.setUser(authService.getCurrentUser());

        return expenseRepository.save(expense);
    }

    // method to get all expenses
    @Override
    public List<Expense> getAllExpenses() {
        return expenseRepository.findAll()
                .stream()
                .sorted(Comparator.comparing(Expense::getDate).reversed())
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

    // method to get an expense by id
    @Override
    public Expense getExpenseById(Long id) {
        Optional<Expense> optionalExpense = expenseRepository.findById(id);
        if (optionalExpense.isPresent()) {
            return optionalExpense.get();
        } else {
            throw new RuntimeException("Expense not found with id: " + id);
        }
    }

    // method to post a new expense
    @Override
    public ExpenseResponseDTO postExpense(ExpenseRequestDTO expenseDTO) {
        Expense savedExpense = saveOrUpdateExpense(new Expense(), expenseDTO);

        // Convert the saved expense to a response DTO
        ExpenseResponseDTO response = new ExpenseResponseDTO();
        response.setId(savedExpense.getId());
        response.setTitle(savedExpense.getTitle());
        response.setDescription(savedExpense.getDescription());
        response.setDate(savedExpense.getDate());
        response.setCategory(savedExpense.getCategory());
        response.setAmount(savedExpense.getAmount());

        return response;
    }

    // method to update an expense
    @Override
    public Expense updateExpense(Long id, ExpenseRequestDTO expenseDTO) {
        Optional<Expense> optionalExpense = expenseRepository.findById(id);
        if (optionalExpense.isPresent()) {
            Expense expense = optionalExpense.get();
            return saveOrUpdateExpense(expense, expenseDTO);
        } else {
            throw new EntityNotFoundException("Expense not found with id: " + id);
        }
    }

    // Method to delete an expense
    @Override
    public void deleteExpense(Long id) {
        // Optionally, check if the expense was deleted successfully
        Optional<Expense> optionalExpense = expenseRepository.findById(id);
        if (optionalExpense.isPresent()) {
            expenseRepository.deleteById(id);
        } else {
            throw new RuntimeException("Expense not found with id: " + id);
        }
    }
}