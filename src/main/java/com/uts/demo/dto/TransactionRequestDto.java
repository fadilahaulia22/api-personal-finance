package com.uts.demo.dto;

import lombok.Builder;
import lombok.Data;


@Data
@Builder

public class TransactionRequestDto {  
    
    private String accountNumber;
    private String customerName;
    private String transactionType;
    private Integer amount;
    private String description;
}
