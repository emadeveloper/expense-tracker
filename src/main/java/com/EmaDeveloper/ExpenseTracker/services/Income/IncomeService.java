package com.EmaDeveloper.ExpenseTracker.services.Income;


import com.EmaDeveloper.ExpenseTracker.dto.IncomeDTO;
import com.EmaDeveloper.ExpenseTracker.entities.Income;

import java.util.List;

public interface IncomeService {

    List<IncomeDTO> getAllIncomes();

    Income postIncome(IncomeDTO incomeDTO);

}
