package com.EmaDeveloper.ExpenseTracker.services.expense;

import com.EmaDeveloper.ExpenseTracker.dto.ExpenseDTO;
import com.EmaDeveloper.ExpenseTracker.entities.Expense;
import com.EmaDeveloper.ExpenseTracker.repository.ExpenseRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.Version;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ExpenseServiceImpl implements ExpenseService {
    private final ExpenseRepository expenseRepository;

    // method to save or update an expense
    private Expense saveOrUpdateExpense(Expense expense, ExpenseDTO expenseDTO) {
        expense.setId(expenseDTO.getId());
        expense.setTitle(expenseDTO.getTitle());
        expense.setDate(expenseDTO.getDate());
        expense.setCategory(expenseDTO.getCategory());
        expense.setAmount(expenseDTO.getAmount());
        expense.setDescription(expenseDTO.getDescription());

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
    public Expense postExpense(ExpenseDTO expenseDTO) {
        return saveOrUpdateExpense(new Expense(), expenseDTO);
    }

    // method to update an expense
    @Override
    public Expense updateExpense(Long id, ExpenseDTO expenseDTO) {
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