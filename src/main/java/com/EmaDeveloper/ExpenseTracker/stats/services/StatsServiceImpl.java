package com.EmaDeveloper.ExpenseTracker.stats.services;

import com.EmaDeveloper.ExpenseTracker.auth.services.AuthService;
import com.EmaDeveloper.ExpenseTracker.expenses.mapper.ExpenseMapper;
import com.EmaDeveloper.ExpenseTracker.incomes.mapper.IncomeMapper;
import com.EmaDeveloper.ExpenseTracker.stats.dto.GraphDTO;
import com.EmaDeveloper.ExpenseTracker.stats.dto.StatsDTO;
import com.EmaDeveloper.ExpenseTracker.expenses.entities.Expense;
import com.EmaDeveloper.ExpenseTracker.incomes.entities.Income;
import com.EmaDeveloper.ExpenseTracker.expenses.repository.ExpenseRepository;
import com.EmaDeveloper.ExpenseTracker.incomes.respository.IncomeRepository;
import com.EmaDeveloper.ExpenseTracker.stats.mapper.StatsMapper;
import com.EmaDeveloper.ExpenseTracker.users.dto.UserSummaryDTO;
import com.EmaDeveloper.ExpenseTracker.users.entities.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;

@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {

    private final ExpenseRepository expenseRepository;
    private final IncomeRepository incomeRepository;
    private final AuthService authService;

    @Override
    public GraphDTO getChartData() {
        User user = authService.getCurrentUser();

        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(27);

        GraphDTO graphDTO = new GraphDTO();

        List<Income> incomes = incomeRepository.findByUserAndDateBetween(user, startDate, endDate);
        List<Expense> expenses = expenseRepository.findByUserAndDateBetween(user, startDate, endDate);

        graphDTO.setExpenseList(StatsMapper.mapToExpenseDTOList(expenses));
        graphDTO.setIncomeList(StatsMapper.mapToIncomeDTOList(incomes));
        graphDTO.setUser(new UserSummaryDTO(user.getId(), user.getUsername(), user.getEmail()));

        return graphDTO;
    }

    @Override
    public StatsDTO getStats() {
        User user = authService.getCurrentUser();

        Double totalIncome = incomeRepository.sumAmountByUser(user);
        Double totalExpense = expenseRepository.sumAmountByUser(user);

        // Prevent NullPointerException by ensuring totals are not null
        totalIncome = totalIncome != null ? totalIncome : 0.0;
        totalExpense = totalExpense != null ? totalExpense : 0.0;

        Optional<Income> latestIncomeOpt = incomeRepository.findFirstByUserOrderByDateDesc(user);
        Optional<Expense> latestExpenseOpt = expenseRepository.findFirstByUserOrderByDateDesc(user);

        List<Income> incomeList = incomeRepository.findByUser(user);
        List<Expense> expenseList = expenseRepository.findByUser(user);

        OptionalDouble minIncome = incomeList.stream().mapToDouble(Income::getAmount).min();
        OptionalDouble maxIncome = incomeList.stream().mapToDouble(Income::getAmount).max();

        OptionalDouble minExpense = expenseList.stream().mapToDouble(Expense::getAmount).min();
        OptionalDouble maxExpense = expenseList.stream().mapToDouble(Expense::getAmount).max();

        StatsDTO statsDTO = new StatsDTO();
        statsDTO.setIncome(totalIncome);
        statsDTO.setExpense(totalExpense);
        statsDTO.setBalance(totalIncome - totalExpense);

        latestIncomeOpt.ifPresent(income -> statsDTO.setLatestIncome(IncomeMapper.toResponseDTO(income)));
        latestExpenseOpt.ifPresent(expense -> statsDTO.setLatestExpense(ExpenseMapper.toResponseDTO(expense)));

        statsDTO.setMinIncome(minIncome.isPresent() ? minIncome.getAsDouble() : null);
        statsDTO.setMaxIncome(maxIncome.isPresent() ? maxIncome.getAsDouble() : null);

        statsDTO.setMinExpense(minExpense.isPresent() ? minExpense.getAsDouble() : null);
        statsDTO.setMaxExpense(maxExpense.isPresent() ? maxExpense.getAsDouble() : null);

        statsDTO.setUser(new UserSummaryDTO(user.getId(), user.getUsername(), user.getEmail()));
        return statsDTO;
    }
}
