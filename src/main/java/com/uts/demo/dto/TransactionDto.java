package com.uts.demo.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor

public class TransactionDto {
  
    @NotNull
    @Size(max = 15)
     String accountNumber;

    @NotNull
    @Size(max = 500)
     String customerName;

    @NotNull
    @Size(max = 10)
     String transactionType;

     @NotNull
     Integer amount;
     
     @Size(max = 500)
     String description;
}
