package com.uts.demo.model;
import java.time.LocalDateTime;

import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "personal_transaction_log")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {
        
    @Id
    @UuidGenerator
    @NotNull
    @Column(name = "id")
    @Size(max = 36)
    private String id;

    @NotNull
    @Column(name = "account_number")
    @Size(max = 15)
    private String accountNumber;

    @NotNull
    @Column(name = "customer_name")
    @Size(max = 500)
    private  String customerName;

    @NotNull
   @Column(name = "transaction_type")
   @Size(max = 10)
   private String transactionType;

   @Column(name = "transaction_date")
   private LocalDateTime transactionDate;

   @NotNull
   @Column(name = "amount")
   private Integer amount;

   @Column(name = "description")
   @Size(max = 500)
   private String description;
}
