package com.EmaDeveloper.ExpenseTracker.expenses.repository;

import com.EmaDeveloper.ExpenseTracker.stats.dto.MonthlyExpensesDTO;
import com.EmaDeveloper.ExpenseTracker.expenses.entities.Expense;
import com.EmaDeveloper.ExpenseTracker.users.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    @Query("""
               SELECT new com.EmaDeveloper.ExpenseTracker.stats.dto.MonthlyExpensesDTO(
                   YEAR(e.date), MONTH(e.date), SUM(e.amount)
               )
               FROM Expense e
               WHERE e.user = :user AND e.date >= :from
               GROUP BY YEAR(e.date), MONTH(e.date)
               ORDER BY YEAR(e.date), MONTH(e.date)
            """)
    List<MonthlyExpensesDTO> sumMonthlyByUserSince(@Param("user") User user,
                                                   @Param("from") LocalDate from);

    @Query("SELECT SUM(e.amount) FROM Expense e WHERE e.user = :user")
    Double sumAmountByUser(@Param("user") User user);

    List<Expense> findByUserAndDateBetween(User user, LocalDate startDate, LocalDate endDate);

    Optional<Expense> findFirstByUserOrderByDateDesc(User user);

    List<Expense> findAllByUserOrderByDateDesc(User user);

    Expense findTopByUserOrderByAmountDesc(User user);

    Expense findTopByUserOrderByAmountAsc(User user);

    List<Expense> findLast5ByUserOrderByDateDesc(User user);
}