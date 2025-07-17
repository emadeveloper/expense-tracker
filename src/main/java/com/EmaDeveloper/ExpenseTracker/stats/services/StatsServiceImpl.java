package com.EmaDeveloper.ExpenseTracker.stats.services;

import com.EmaDeveloper.ExpenseTracker.incomes.dto.IncomeResponseDTO;
import com.EmaDeveloper.ExpenseTracker.stats.dto.GraphDTO;
import com.EmaDeveloper.ExpenseTracker.stats.dto.StatsDTO;
import com.EmaDeveloper.ExpenseTracker.expenses.entities.Expense;
import com.EmaDeveloper.ExpenseTracker.incomes.entities.Income;
import com.EmaDeveloper.ExpenseTracker.expenses.repository.ExpenseRepository;
import com.EmaDeveloper.ExpenseTracker.incomes.respository.IncomeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;

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

    @Override
    public StatsDTO getStats() {
        Double totalIncome = incomeRepository.sumAllAmounts();
        Double totalExpense = expenseRepository.sumAllAmounts();

        Optional<Income> optionalIncome = incomeRepository.findFirstByOrderByDateDesc();
        Optional<Expense> optionalExpense = expenseRepository.findFirstByOrderByDateDesc();

        StatsDTO statsDTO = new StatsDTO();

        statsDTO.setIncome(totalIncome);
        statsDTO.setExpense(totalExpense);

        statsDTO.setBalance(totalIncome - totalExpense);

        optionalIncome.ifPresent(statsDTO::setLatestIncome);
        optionalExpense.ifPresent(statsDTO::setLatestExpense);

        List<Income> incomeList = Optional.of(incomeRepository.findAll()).orElse(Collections.emptyList());
        List<Expense> expenseList = Optional.of(expenseRepository.findAll()).orElse(Collections.emptyList());

        OptionalDouble minIncome = incomeList.stream().mapToDouble(Income::getAmount).min();
        OptionalDouble maxIncome = incomeList.stream().mapToDouble(Income::getAmount).max();

        OptionalDouble minExpense = expenseList.stream().mapToDouble(Expense::getAmount).min();
        OptionalDouble maxExpense = expenseList.stream().mapToDouble(Expense::getAmount).max();

        statsDTO.setMaxIncome(maxIncome.isPresent() ? maxIncome.getAsDouble() : null);
        statsDTO.setMinIncome(minIncome.isPresent() ? minIncome.getAsDouble() : null);

        statsDTO.setMinExpense(minExpense.isPresent() ? minExpense.getAsDouble() : null);
        statsDTO.setMaxExpense(maxExpense.isPresent() ? maxExpense.getAsDouble() : null);

        return statsDTO;
    }
}
