package com.uts.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.uts.demo.model.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction,String>{
    
    // kalo pake hql pake database yang ada di entity
    // @Query("select t from Transaction t " +
    // "where t.customerName like %:customerName% " +
    // "order by t.accountNumber asc")
    // List<Transaction> findAllOrderByAccountNumberUsingHql(String customerName);


    // @Query("select t from Transaction t where t.id = :id")
    // Transaction findByIdUsingHql(String id);

    List<Transaction> findByAccountNumber(String accountNumber);

    List<Transaction> findByCustomerName(String customerName);

}
