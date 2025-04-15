package com.EmaDeveloper.ExpenseTracker.services.Income;

import com.EmaDeveloper.ExpenseTracker.dto.IncomeDTO;
import com.EmaDeveloper.ExpenseTracker.entities.Income;
import com.EmaDeveloper.ExpenseTracker.repository.IncomeRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class IncomeServiceImpl implements IncomeService {
    private final IncomeRepository incomeRepository;

    // method to save or update an income
    private Income saveOrUpdateIncome(Income income, IncomeDTO incomeDTO) {
        income.setId(incomeDTO.getId());
        income.setTitle(incomeDTO.getTitle());
        income.setDate(incomeDTO.getDate());
        income.setCategory(incomeDTO.getCategory());
        income.setAmount(incomeDTO.getAmount());
        income.setDescription(incomeDTO.getDescription());

        return incomeRepository.save(income);
    }

    // method to get all incomes
    @Override
    public List<IncomeDTO> getAllIncomes() {
        return incomeRepository.findAll()
                .stream()
                .sorted(Comparator.comparing(Income::getDate).reversed())
                .map(Income::getIncomeDTO)
                .toList();
    }

    // method to get income by id
    @Override
    public Income getIncomeById(Long id) {
        Optional<Income> optionalIncome = incomeRepository.findById(id);
        if (optionalIncome.isPresent()) {
            return optionalIncome.get();
        } else {
            throw new EntityNotFoundException("Income not found with id: " + id);
        }
    }

    // method to create a new income
    @Override
    public Income postIncome(IncomeDTO incomeDTO) {
        return saveOrUpdateIncome(new Income(), incomeDTO);
    }

    // method to update an existing income
    @Override
    public Income updateIncome(Long id, IncomeDTO incomeDTO) {
        Optional<Income> optionalIncome = incomeRepository.findById(id);

        if (optionalIncome.isPresent()) {
            Income income = optionalIncome.get();
            return saveOrUpdateIncome(income, incomeDTO);
        } else {
            throw new EntityNotFoundException("Income not found with id: " + id);
        }
    }
}
