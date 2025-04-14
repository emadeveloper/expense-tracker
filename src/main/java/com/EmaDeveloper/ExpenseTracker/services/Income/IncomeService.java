package com.EmaDeveloper.ExpenseTracker.services.Income;


import com.EmaDeveloper.ExpenseTracker.dto.IncomeDTO;
import com.EmaDeveloper.ExpenseTracker.entities.Income;

public interface IncomeService {

    Income postIncome(IncomeDTO incomeDTO);

}
