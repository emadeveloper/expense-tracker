package com.EmaDeveloper.ExpenseTracker.services.stats;

import com.EmaDeveloper.ExpenseTracker.dto.GraphDTO;
import com.EmaDeveloper.ExpenseTracker.repository.ExpenseRepository;
import com.EmaDeveloper.ExpenseTracker.repository.IncomeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor

public class StatsServiceImpl implements StatsService{

    private final ExpenseRepository expenseRepository;

    private final IncomeRepository incomeRepository;

    public GraphDTO getChartData(){
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = LocalDate.now().minusDays(27);

        GraphDTO graphDTO = new GraphDTO();
        graphDTO.setExpenseList(expenseRepository.findByDateBetween(startDate, endDate));
        graphDTO.setIncomeList(incomeRepository.findByDateBetween(startDate, endDate));

        return graphDTO;
    }
}
