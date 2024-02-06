package com.uts.demo.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uts.demo.dto.TransactionDto;
import com.uts.demo.dto.TransactionRequestDto;
import com.uts.demo.model.Transaction;
import com.uts.demo.repository.TransactionRepository;
import com.uts.demo.dto.TransactionResponseDto;



@Service
public class TransactionImpl implements TransactionServise{
    
    @Autowired
    TransactionRepository repository;
    
    @Override
    public String create(TransactionDto dto) {
       if (!check(dto.getAccountNumber())){
            // nama sama di database
            List<Transaction> transactionsWithSameName = repository.findByCustomerName(dto.getCustomerName());
            for (Transaction tr : transactionsWithSameName) {
                if (tr.getCustomerName().equals(dto.getCustomerName())) {
                    return "Failed! name already exist.";
                }
            }

            if (!dto.getTransactionType().equalsIgnoreCase("debit") && !dto.getTransactionType().equalsIgnoreCase("kredit")) {
                return "Failed! Invalid transaction type. Transaction type should be 'debit' or 'kredit'.";
            }else{
                // Nomor akun belum ada di database, maka langsung simpan data baru
                    repository.save(Transaction.builder()
                        // .id(dto.getId())
                        .accountNumber(dto.getAccountNumber())
                        .customerName(dto.getCustomerName())
                        .transactionType(dto.getTransactionType())
                        .transactionDate(LocalDateTime.now().truncatedTo(java.time.temporal.ChronoUnit.SECONDS))
                        .amount(dto.getAmount())
                        .description(dto.getDescription())
                        .build());
                return "Success!";
            }
        }else{
            // Nomor akun sudah ada di database, periksa apakah nama pelanggan sesuai
            List<Transaction> transactionsWithSameAccount = repository.findByAccountNumber(dto.getAccountNumber());
            for (Transaction transaction : transactionsWithSameAccount) {
                // Jika number account ditemukan untuk nama akun yang sama
                if (transaction.getCustomerName().equals(dto.getCustomerName())) {
                    if (!dto.getTransactionType().equalsIgnoreCase("debit") && !dto.getTransactionType().equalsIgnoreCase("kredit")) {
                        return "Failed! Invalid transaction type. Transaction type should be 'debit' or 'kredit'.";
                    }else{
                        repository.save(Transaction.builder()
                        // .id(dto.getId())
                        .accountNumber(dto.getAccountNumber())
                        .customerName(dto.getCustomerName())
                        .transactionType(dto.getTransactionType())
                        .transactionDate(LocalDateTime.now().truncatedTo(java.time.temporal.ChronoUnit.SECONDS))
                        .amount(dto.getAmount())
                        .description(dto.getDescription())
                        .build());
                    return "Success! Account number already exists with a same customer name.";
                    }
                }
            }
            // Jika ditemukan nama yang berbeda dengan nomor akun yang diberikan
            return "Failed! Account number not same  with the customer name.";
        }
    }

            // REMOVE========
        @Override
        public String remove(String id) {
            Transaction transaction = repository.findById(id).orElse(null);
        
                if (transaction!=null) {
                    repository.delete(transaction);
                    return "Data with ID " + id + " removed successfully";
                }else{
                    return "Data with ID " + id + " not found, cannot be removed";
                }
        }


        @Override
        public List<TransactionResponseDto> view() {
            return repository.findAll()
                    .stream()
                    .map(this::toDto)
                    .toList();
        }

    public TransactionResponseDto toDto(Transaction transaction) {
        return TransactionResponseDto.builder()
                .id(transaction.getId())
                .accountNumber(transaction.getAccountNumber())
                .customerName(transaction.getCustomerName())
                .transactionType(transaction.getTransactionType())
                .transactionDate(LocalDateTime.now().truncatedTo(java.time.temporal.ChronoUnit.SECONDS))
                .amount(transaction.getAmount())
                .description(transaction.getDescription())
                .build();
    }
    @Override
    public boolean check(String accountNumber) {
        List<Transaction> transactions = repository.findAll(); // Mengambil semua transaksi dari database

        for (Transaction transaction : transactions) {
            if (transaction.getAccountNumber().equals(accountNumber)) {
                return true; // Jika nomor akun sudah ada di database, kembalikan true
            }
        }
        return false; 
    }

    @Override
    public String update(String id, TransactionRequestDto dto) {
        Transaction tr = repository.findById(id).orElse(null);

        if(tr !=null){
            tr.setAccountNumber(dto.getAccountNumber());
            tr.setCustomerName(dto.getCustomerName());
            tr.setTransactionType(dto.getTransactionType());
            tr.setAmount(dto.getAmount());
            tr.setDescription(dto.getDescription());

                return "Successfully update from list";
        } else{
            return "Failed , Id not found";
        }   
    }

    // @Override
    // public TransactionDto findById(String id){
    //     Transaction tr = repository.findByIdUsingHql(id);
    //     if(tr != null){
    //         TransactionDto dto = new TransactionDto();
    //         // dto.setId(tr.getId());
    //         dto.setAccountNumber(tr.getAccountNumber());
    //         dto.setCustomerName(tr.getCustomerName());
    //         dto.setTransactionType(tr.getTransactionType());
    //         dto.setAmount(tr.getAmount());
    //         dto.setDescription(tr.getDescription()); 
    //         return dto;
    //     }else{
    //         return null;
    //     }
    // }
}
