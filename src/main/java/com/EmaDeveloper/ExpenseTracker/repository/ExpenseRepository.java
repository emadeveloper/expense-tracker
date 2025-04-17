package com.EmaDeveloper.ExpenseTracker.repository;

import com.EmaDeveloper.ExpenseTracker.entities.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository <Expense, Long>{
    List<Expense> findByDateBetween(LocalDate startDate, LocalDate endDate);
}
