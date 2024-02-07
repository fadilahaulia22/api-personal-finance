package com.uts.demo.service;

import java.time.LocalDateTime;
import java.util.List;

import com.uts.demo.dto.CustomerFinancialSummaryDto;
import com.uts.demo.dto.CustomerSummaryResponseDto;
import com.uts.demo.dto.TransactionDto;
import com.uts.demo.dto.TransactionRequestDto;
import com.uts.demo.dto.TransactionResponseDto;


public interface TransactionServise {
    String create(TransactionDto dto);

    String remove(String id);
    
    List<TransactionResponseDto> view();

    boolean check(String accountNumber);

    String update(String id,TransactionRequestDto dto);

    CustomerFinancialSummaryDto getCustomerFinancialSummary(String accountNumber, LocalDateTime startDate, LocalDateTime endDate);

    CustomerSummaryResponseDto responseDto(String accountNumber);

    CustomerSummaryResponseDto montly(String accountNumber);

    CustomerSummaryResponseDto year(String accountNumber);

    CustomerFinancialSummaryDto deviation(String accountNumber, LocalDateTime startDate, LocalDateTime endDate);
    
    List<CustomerFinancialSummaryDto> viewDeviationAllCustomer(LocalDateTime startDate, LocalDateTime endDate);
}
