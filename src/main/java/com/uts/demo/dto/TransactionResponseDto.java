package com.uts.demo.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionResponseDto {
    @NotNull
    @Size(max = 36)
     String id;

    @NotNull
    @Size(max = 15)
     String accountNumber;

    @NotNull
    @Size(max = 500)
     String customerName;

    @NotNull
    @Size(max = 10)
     String transactionType;

    LocalDateTime transactionDate;

     @NotNull
     Integer amount;
     
     @Size(max = 500)
     String description;
}
