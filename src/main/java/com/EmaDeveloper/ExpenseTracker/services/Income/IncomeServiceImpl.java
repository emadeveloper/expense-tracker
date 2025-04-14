package com.EmaDeveloper.ExpenseTracker.services.Income;

import com.EmaDeveloper.ExpenseTracker.dto.IncomeDTO;
import com.EmaDeveloper.ExpenseTracker.entities.Income;
import com.EmaDeveloper.ExpenseTracker.repository.IncomeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

    @Override
    public Income postIncome(IncomeDTO incomeDTO) {
        return saveOrUpdateIncome(new Income(), incomeDTO);
    }
}
