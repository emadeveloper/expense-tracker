package com.EmaDeveloper.ExpenseTracker.incomes.services;


import com.EmaDeveloper.ExpenseTracker.incomes.dto.IncomeRequestDTO;
import com.EmaDeveloper.ExpenseTracker.incomes.dto.IncomeResponseDTO;

import java.util.List;

public interface IncomeService {

    List<IncomeResponseDTO> getAllIncomes();

    List<IncomeResponseDTO> getAllIncomesByCurrentUser();

    IncomeResponseDTO getIncomeById(Long id);

    IncomeResponseDTO postIncome(IncomeRequestDTO incomeDTO);

    IncomeResponseDTO updateIncome(Long id, IncomeRequestDTO incomeDTO);

    void deleteIncome(Long id);
}
