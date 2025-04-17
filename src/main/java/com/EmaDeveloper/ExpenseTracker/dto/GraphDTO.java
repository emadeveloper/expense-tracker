package com.EmaDeveloper.ExpenseTracker.dto;

import com.EmaDeveloper.ExpenseTracker.entities.Expense;
import com.EmaDeveloper.ExpenseTracker.entities.Income;
import lombok.Data;

import java.util.List;

@Data
public class GraphDTO {

    private List<Expense> expenseList;

    private List<Income> incomeList;
}
