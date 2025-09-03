package com.EmaDeveloper.ExpenseTracker.stats.services;

import com.EmaDeveloper.ExpenseTracker.auth.services.AuthService;
import com.EmaDeveloper.ExpenseTracker.expenses.entities.Expense;
import com.EmaDeveloper.ExpenseTracker.expenses.mapper.ExpenseMapper;
import com.EmaDeveloper.ExpenseTracker.expenses.repository.ExpenseRepository;
import com.EmaDeveloper.ExpenseTracker.incomes.entities.Income;
import com.EmaDeveloper.ExpenseTracker.incomes.mapper.IncomeMapper;
import com.EmaDeveloper.ExpenseTracker.incomes.respository.IncomeRepository;
import com.EmaDeveloper.ExpenseTracker.stats.dto.*;
import com.EmaDeveloper.ExpenseTracker.stats.mapper.StatsMapper;
import com.EmaDeveloper.ExpenseTracker.users.dto.UserSummaryDTO;
import com.EmaDeveloper.ExpenseTracker.users.entities.User;
import lombok.RequiredArgsConstructor;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.EmaDeveloper.ExpenseTracker.stats.mapper.StatsMapper.toMinMaxDTO;

@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {

    private final ExpenseRepository expenseRepository;
    private final IncomeRepository incomeRepository;
    private final AuthService authService;

    @Override
    public GraphDTO getChartData(User user) {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(27);

        List<Income> incomes = incomeRepository.findByUserAndDateBetween(user, startDate, endDate);
        List<Expense> expenses = expenseRepository.findByUserAndDateBetween(user, startDate, endDate);

        GraphDTO graphDTO = new GraphDTO();
        graphDTO.setExpenseList(StatsMapper.mapToExpenseDTOList(expenses));
        graphDTO.setIncomeList(StatsMapper.mapToIncomeDTOList(incomes));
        graphDTO.setUser(new UserSummaryDTO(user.getId(), user.getUsername(), user.getEmail()));

        return graphDTO;
    }

    @Override
    public StatsDTO getStats(User user) {
      // Incomes
     Double totalIncomes = incomeRepository.sumAmountByUser(user);

     Income minIncomeEntity = incomeRepository.findTopByUserOrderByAmountAsc(user);
     Income maxIncomeEntity = incomeRepository.findTopByUserOrderByAmountDesc(user);

     Optional<Income> latestIncome = incomeRepository.findFirstByUserOrderByDateDesc(user);

     IncomeStatsDTO incomeStats = new IncomeStatsDTO(
            toMinMaxDTO(minIncomeEntity),
             toMinMaxDTO(maxIncomeEntity)
     );

     // Expenses
        Double totalExpenses = expenseRepository.sumAmountByUser(user);

        Expense minExpenseEntity = expenseRepository.findTopByUserOrderByAmountAsc(user);
        Expense maxExpenseEntity = expenseRepository.findTopByUserOrderByAmountDesc(user);

        Optional<Expense> latestExpense = expenseRepository.findFirstByUserOrderByDateDesc(user);

        ExpenseStatsDTO expenseStats = new ExpenseStatsDTO(
                toMinMaxDTO(minExpenseEntity),
                toMinMaxDTO(maxExpenseEntity)
        );

        // Balance
        Double balance = (totalIncomes == null ? 0 : totalIncomes) - (totalExpenses == null ? 0 : totalExpenses);

        // Monthly Expenses
        LocalDate startOfYear = LocalDate.now().withDayOfYear(1);
        List<MonthlyExpensesDTO> monthlyExpenses = expenseRepository.sumMonthlyByUserSince(user, startOfYear);

        // Return StatsDTO
        StatsDTO statsDTO = new StatsDTO();

        statsDTO.setBalance(balance);
        statsDTO.setTotalIncomes(totalIncomes);
        statsDTO.setTotalExpenses(totalExpenses);

        statsDTO.setMonthlyExpenses(monthlyExpenses);

        statsDTO.setIncomes(incomeStats);
        statsDTO.setExpenses(expenseStats);
        statsDTO.setUser(new UserSummaryDTO(user.getId(), user.getUsername(), user.getEmail()));

        latestIncome.ifPresent(income -> statsDTO.setLatestIncome(IncomeMapper.toResponseDTO(income)));
        latestExpense.ifPresent(expense -> statsDTO.setLatestExpense(ExpenseMapper.toResponseDTO(expense)));

        return statsDTO;
    }

}
