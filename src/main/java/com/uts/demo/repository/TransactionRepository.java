package com.uts.demo.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.uts.demo.model.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction,String>{

    List<Transaction> findByAccountNumber(String accountNumber);

    List<Transaction> findByCustomerName(String customerName);

    List<Transaction> findByAccountNumberAndTransactionDateBetween(String accountNumber, LocalDateTime startDate, LocalDateTime endDate);
    
    boolean existsByAccountNumber(String accountNumber);
    
    List<Transaction> findByTransactionDateBetween(LocalDateTime startDate, LocalDateTime endDate);


}
