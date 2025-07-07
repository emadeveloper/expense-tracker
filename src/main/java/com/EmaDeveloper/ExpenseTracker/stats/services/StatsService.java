package com.EmaDeveloper.ExpenseTracker.stats.services;

import com.EmaDeveloper.ExpenseTracker.stats.dto.GraphDTO;
import com.EmaDeveloper.ExpenseTracker.stats.dto.StatsDTO;

public interface StatsService {

    GraphDTO getChartData();

    StatsDTO getStats();
}
