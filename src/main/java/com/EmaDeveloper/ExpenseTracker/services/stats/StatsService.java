package com.EmaDeveloper.ExpenseTracker.services.stats;

import com.EmaDeveloper.ExpenseTracker.dto.GraphDTO;
import com.EmaDeveloper.ExpenseTracker.dto.StatsDTO;

public interface StatsService {

    GraphDTO getChartData();

    StatsDTO getStats();
}
