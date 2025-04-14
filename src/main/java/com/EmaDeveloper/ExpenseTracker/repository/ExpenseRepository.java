package com.EmaDeveloper.ExpenseTracker.repository;

import com.EmaDeveloper.ExpenseTracker.entities.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExpenseRepository extends JpaRepository <Expense, Long>{
}
