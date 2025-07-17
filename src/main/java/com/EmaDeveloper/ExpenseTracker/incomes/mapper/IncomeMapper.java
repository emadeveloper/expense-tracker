package com.EmaDeveloper.ExpenseTracker.incomes.mapper;

import com.EmaDeveloper.ExpenseTracker.incomes.dto.IncomeRequestDTO;
import com.EmaDeveloper.ExpenseTracker.incomes.dto.IncomeResponseDTO;
import com.EmaDeveloper.ExpenseTracker.incomes.entities.Income;
import com.EmaDeveloper.ExpenseTracker.users.dto.UserSummaryDTO;
import com.EmaDeveloper.ExpenseTracker.users.entities.User;

import java.time.LocalDate;

public class IncomeMapper {
    public static Income toEntity(IncomeRequestDTO incomeRequestDTO, User user) {
        Income income = new Income();
        income.setDate(incomeRequestDTO.getDate() != null ? incomeRequestDTO.getDate() : LocalDate.now());
        income.setTitle(incomeRequestDTO.getTitle());
        income.setDescription(incomeRequestDTO.getDescription());
        income.setCategory(incomeRequestDTO.getCategory());
        income.setAmount(incomeRequestDTO.getAmount());
        income.setUser(user);

        return income;
    }

    public static IncomeResponseDTO toResponseDTO(Income income){
        IncomeResponseDTO responseDTO = new IncomeResponseDTO();
        responseDTO.setId(income.getId());
        responseDTO.setTitle(income.getTitle());
        responseDTO.setDescription(income.getDescription());
        responseDTO.setCategory(income.getCategory());
        responseDTO.setAmount(income.getAmount());
        responseDTO.setDate(income.getDate() != null ? income.getDate() : LocalDate.now());

        UserSummaryDTO userSummary = new UserSummaryDTO();
        userSummary.setId(income.getUser().getId());
        userSummary.setUsername(income.getUser().getUsername());
        userSummary.setEmail(income.getUser().getEmail());

        responseDTO.setUser(userSummary);
        return responseDTO;
    }
}

