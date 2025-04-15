package com.EmaDeveloper.ExpenseTracker.services.Income;


import com.EmaDeveloper.ExpenseTracker.dto.IncomeDTO;
import com.EmaDeveloper.ExpenseTracker.entities.Income;

import java.util.List;

public interface IncomeService {

    List<IncomeDTO> getAllIncomes();

    Income getIncomeById(Long id);

    Income postIncome(IncomeDTO incomeDTO);

    Income updateIncome(Long id, IncomeDTO incomeDTO);

}
