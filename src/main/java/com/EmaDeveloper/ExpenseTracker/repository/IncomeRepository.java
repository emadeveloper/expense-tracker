package com.EmaDeveloper.ExpenseTracker.repository;

import com.EmaDeveloper.ExpenseTracker.entities.Income;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface IncomeRepository extends JpaRepository<Income, Long> {
    List<Income> findByDateBetween(LocalDate startDate, LocalDate endDate);
}
