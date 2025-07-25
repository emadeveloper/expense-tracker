package com.EmaDeveloper.ExpenseTracker.incomes.services;

import com.EmaDeveloper.ExpenseTracker.auth.services.AuthService;
import com.EmaDeveloper.ExpenseTracker.exceptions.custom.IncomeNotFoundException;
import com.EmaDeveloper.ExpenseTracker.incomes.dto.IncomeRequestDTO;
import com.EmaDeveloper.ExpenseTracker.incomes.dto.IncomeResponseDTO;
import com.EmaDeveloper.ExpenseTracker.incomes.entities.Income;
import com.EmaDeveloper.ExpenseTracker.incomes.mapper.IncomeMapper;
import com.EmaDeveloper.ExpenseTracker.incomes.respository.IncomeRepository;
import com.EmaDeveloper.ExpenseTracker.users.entities.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class IncomeServiceImpl implements IncomeService {
    private final IncomeRepository incomeRepository;
    private final AuthService authService;

    // method to get all incomes only accessible by ADMIN
    @Override
    public List<IncomeResponseDTO> getAllIncomes() {
        return incomeRepository.findAll()
                .stream()
                .map(IncomeMapper::toResponseDTO)
                .toList();
    }

    // method to get all incomes by the current user
    @Override
    public List<IncomeResponseDTO> getAllIncomesByCurrentUser() {
        User user = authService.getCurrentUser();

        return incomeRepository.findAllByUserOrderByDateDesc(user)
                .stream()
                .map(IncomeMapper::toResponseDTO)
                .toList();
    }

    // method to get income by id
    @Override
    public IncomeResponseDTO getIncomeById(Long id) {
        User currentUser = authService.getCurrentUser();

        Income income = incomeRepository.findById(id)
                .orElseThrow(() -> new IncomeNotFoundException(id));

        if (!income.getUser().getId().equals(currentUser.getId())){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have permission to access this income");
        }

        return IncomeMapper.toResponseDTO(income);
    }

    // method to create a new income
    @Override
    public IncomeResponseDTO postIncome(IncomeRequestDTO incomeDTO) {
        User user = authService.getCurrentUser();
        Income income = IncomeMapper.toEntity(incomeDTO, user);

        Income savedIncome = incomeRepository.save(income);

        return IncomeMapper.toResponseDTO(savedIncome);
    }

    // method to update an existing income
    @Override
    public IncomeResponseDTO updateIncome(Long id, IncomeRequestDTO incomeDTO) {
        User currentUser = authService.getCurrentUser();
        Income income = incomeRepository.findById(id)
                .orElseThrow(() -> new IncomeNotFoundException(id));

        // Check if the income belongs to the current user
        if (!income.getUser().getId().equals(currentUser.getId())){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have permission to update this income");
        }

        income.setTitle(incomeDTO.getTitle());
        income.setDescription(incomeDTO.getDescription());
        income.setCategory(incomeDTO.getCategory());
        income.setAmount(incomeDTO.getAmount());
        income.setDate(incomeDTO.getDate());

        Income updatedIncome = incomeRepository.save(income);

        return IncomeMapper.toResponseDTO(updatedIncome);
    }

    // method to delete an income by id
    @Override
    public void deleteIncome(Long id) {
        if (!incomeRepository.existsById(id)) {
            throw new IncomeNotFoundException(id);
        }

        // Check if the income belongs to the current user
        User currentUser = authService.getCurrentUser();

        Optional<Income> optionalIncome = incomeRepository.findById(id);
        if (optionalIncome.isPresent() && !optionalIncome.get().getUser().equals(currentUser)) {
            throw new SecurityException("You do not have permission to delete this expense");
        }
        // Delete the income
        incomeRepository.deleteById(id);
    }

}
