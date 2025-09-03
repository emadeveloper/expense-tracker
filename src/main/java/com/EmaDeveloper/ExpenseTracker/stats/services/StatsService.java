package com.EmaDeveloper.ExpenseTracker.stats.services;

import com.EmaDeveloper.ExpenseTracker.stats.dto.GraphDTO;
import com.EmaDeveloper.ExpenseTracker.stats.dto.MonthlyExpensesDTO;
import com.EmaDeveloper.ExpenseTracker.stats.dto.StatsDTO;
import com.EmaDeveloper.ExpenseTracker.users.entities.User;

import java.util.List;

public interface StatsService {

    GraphDTO getChartData(User user);

    StatsDTO getStats(User user);
}
