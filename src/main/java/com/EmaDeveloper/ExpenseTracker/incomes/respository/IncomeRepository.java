package com.EmaDeveloper.ExpenseTracker.incomes.respository;

import com.EmaDeveloper.ExpenseTracker.incomes.entities.Income;
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

    List<Income> findByUserAndDateBetween(User user, LocalDate startDate, LocalDate endDate);

    @Query("SELECT SUM(i.amount) FROM Income i")
    Double sumAmountByUser(@Param("user") User user);

    Optional<Income> findFirstByUserOrderByDateDesc(User user);

    List<Income> findAllByUserOrderByDateDesc(User user);

    List<Income> findByUser(User user);
}
