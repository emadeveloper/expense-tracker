package com.EmaDeveloper.ExpenseTracker.incomes.services;


import com.EmaDeveloper.ExpenseTracker.incomes.dto.IncomeDTO;
import com.EmaDeveloper.ExpenseTracker.incomes.entities.Income;

import java.util.List;

public interface IncomeService {

    List<IncomeDTO> getAllIncomes();

    Income getIncomeById(Long id);

    Income postIncome(IncomeDTO incomeDTO);

    Income updateIncome(Long id, IncomeDTO incomeDTO);

    void deleteIncome(Long id);
}
