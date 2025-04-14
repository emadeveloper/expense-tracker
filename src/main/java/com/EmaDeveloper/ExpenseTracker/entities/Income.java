package com.EmaDeveloper.ExpenseTracker.entities;

import com.EmaDeveloper.ExpenseTracker.dto.IncomeDTO;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
public class Income {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    private Double amount;

    private LocalDate date;

    private String category;

    public IncomeDTO getIncomeDTO() {
        IncomeDTO incomeDTO = new IncomeDTO();
        incomeDTO.setId(id);
        incomeDTO.setTitle(title);
        incomeDTO.setDescription(description);
        incomeDTO.setAmount(amount);
        incomeDTO.setDate(date);
        incomeDTO.setCategory(category);

        return incomeDTO;
    }
}
