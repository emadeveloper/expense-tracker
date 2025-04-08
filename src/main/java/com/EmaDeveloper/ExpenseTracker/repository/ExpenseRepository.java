package com.EmaDeveloper.ExpenseTracker.repository;

import com.EmaDeveloper.ExpenseTracker.entities.Expense;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpenseRepository extends JpaRepository <Expense, Long>{
}
