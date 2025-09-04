package com.EmaDeveloper.ExpenseTracker.incomes.respository;

import com.EmaDeveloper.ExpenseTracker.incomes.entities.Income;
import com.EmaDeveloper.ExpenseTracker.stats.dto.MonthlyIncomesDTO;
import com.EmaDeveloper.ExpenseTracker.users.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface IncomeRepository extends JpaRepository<Income, Long> {

    @Query("""
               SELECT new com.EmaDeveloper.ExpenseTracker.stats.dto.MonthlyIncomesDTO(
                   YEAR(i.date), MONTH(i.date), SUM(i.amount)
               )
               FROM Income i
               WHERE i.user = :user AND i.date >= :from
               GROUP BY YEAR(i.date), MONTH(i.date)
               ORDER BY YEAR(i.date), MONTH(i.date)
            """)
    List<MonthlyIncomesDTO> sumMonthlyByUserSince(@Param("user") User user,
                                                  @Param("from") LocalDate from);

    @Query("SELECT SUM(i.amount) FROM Income i WHERE i.user = :user")
    Double sumAmountByUser(@Param("user") User user);

    List<Income> findByUserAndDateBetween(User user, LocalDate startDate, LocalDate endDate);

    Optional<Income> findFirstByUserOrderByDateDesc(User user);

    List<Income> findAllByUserOrderByDateDesc(User user);

    Income findTopByUserOrderByAmountDesc(User user);

    Income findTopByUserOrderByAmountAsc(User user);
}
