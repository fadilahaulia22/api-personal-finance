package com.uts.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerSummaryResponseDto {
    private String accountNumber;
    private String customerName;
    private String periode;
    private int totalExpense;
    private int totalIncome;
}
