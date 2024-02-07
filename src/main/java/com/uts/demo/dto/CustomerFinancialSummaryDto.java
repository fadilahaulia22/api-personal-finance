package com.uts.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerFinancialSummaryDto {
    private String accountNumber;
    private String customerName;
    private int totalExpense;
    private int totalIncome;
    
    private int deviation;
    private String conclusion;
}

