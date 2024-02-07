package com.uts.demo.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uts.demo.dto.CustomerFinancialSummaryDto;
import com.uts.demo.dto.CustomerSummaryResponseDto;
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
                .transactionDate(transaction.getTransactionDate())
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

        if (!dto.getTransactionType().equalsIgnoreCase("debit") && !dto.getTransactionType().equalsIgnoreCase("kredit")) {
            return "Failed! Invalid transaction type. Transaction type should be 'debit' or 'kredit'.";
        }else{
            Transaction tr = repository.findById(id).orElse(null);
            if(tr !=null){
                tr.setAccountNumber(dto.getAccountNumber());
                tr.setCustomerName(dto.getCustomerName());
                tr.setTransactionType(dto.getTransactionType());
                tr.setAmount(dto.getAmount());
                tr.setDescription(dto.getDescription());
                // untuk menyimpan kedatabase
                    repository.save(tr);
                    return "Successfully updated data with ID " + id;
            } else{
               return "Failed, data with ID " + id + " not found";       
            }   
        }
    }




    @Override
    public CustomerFinancialSummaryDto getCustomerFinancialSummary(String accountNumber, LocalDateTime startDate, LocalDateTime endDate) {
        // periksa apakah endDate lebih kecil dari startDate
        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("Maaf, tanggal akhir tidak boleh lebih kecil dari tanggal awal");
        }
        List<Transaction> transactions = repository.findByAccountNumberAndTransactionDateBetween(accountNumber, startDate, endDate);

        int totalExpense = 0;
        int totalIncome = 0;
        String customerName = null;

        for (Transaction transaction : transactions) {
            if (transaction.getTransactionType().equalsIgnoreCase("debit")) {
                totalExpense += transaction.getAmount();
            } else if (transaction.getTransactionType().equalsIgnoreCase("kredit")) {
                totalIncome += transaction.getAmount();
            }
             // Mendapatkan nama pelanggan dari transaksi pertama dalam rentang waktu
                if (customerName == null && transaction.getCustomerName() != null) {
                    customerName = transaction.getCustomerName();
                }
        }

        CustomerFinancialSummaryDto summaryDto = new CustomerFinancialSummaryDto();

        summaryDto.setAccountNumber(accountNumber);
        summaryDto.setCustomerName(customerName); // Mengatur nama pelanggan di DTO
        summaryDto.setTotalExpense(totalExpense);
        summaryDto.setTotalIncome(totalIncome);
        
        return summaryDto;
    }


    @Override
    public CustomerSummaryResponseDto responseDto(String accountNumber) {
        // Validasi apakah nomor akun ada dalam database
        boolean isAccountExist = repository.existsByAccountNumber(accountNumber);
        if (!isAccountExist) {
            throw new IllegalArgumentException("account number is not found!");
        }
            // Ambil transaksi berdasarkan account number
        List<Transaction> transactions = repository.findByAccountNumber(accountNumber);

        int totalExpense = 0;
        int totalIncome = 0;
        String customerName = null;

        for (Transaction transaction : transactions) {
            if (transaction.getTransactionType().equalsIgnoreCase("debit")) {
                totalExpense += transaction.getAmount();
            } else if (transaction.getTransactionType().equalsIgnoreCase("kredit")) {
                totalIncome += transaction.getAmount();
            }
            if (customerName == null && transaction.getCustomerName() != null) {
                customerName = transaction.getCustomerName();
            }
        }
// ambil tanggal saat ini
        LocalDateTime currentDate = LocalDateTime.now();
                // Format tanggal menjadi "Hari, DD MMMM YYYY"
        DateTimeFormatter format = DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy");
        String formattedDate = currentDate.format(format);

        CustomerSummaryResponseDto responseDto = new CustomerSummaryResponseDto();
        responseDto.setAccountNumber(accountNumber);
        responseDto.setCustomerName(customerName);
        responseDto.setPeriode(formattedDate);
        responseDto.setTotalExpense(totalExpense);
        responseDto.setTotalIncome(totalIncome);

        return responseDto;
    }
    

    @Override
    public CustomerSummaryResponseDto montly(String accountNumber){
        boolean isAccountExist = repository.existsByAccountNumber(accountNumber);
        if (!isAccountExist) {
            throw new IllegalArgumentException("account number is not found!");
        }

        List<Transaction> transactions = repository.findByAccountNumber(accountNumber);
        int totalExpense = 0;
        int totalIncome = 0;
        String customerName = null;

        for (Transaction t : transactions) {
            if (t.getTransactionType().equalsIgnoreCase("debit")) {
                totalExpense += t.getAmount();
            }else if (t.getTransactionType().equalsIgnoreCase("kredit")) {
                totalIncome += t.getAmount();
            }
            if (customerName == null && t.getCustomerName() != null) {
                customerName = t.getCustomerName();
            }
        }

         // Ambil bulan dan tahun saat ini
        LocalDate currentDate = LocalDate.now();
        // Format tanggal menjadi "MMMM YYYY"
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy");
        String formattedDate = currentDate.format(formatter);

        CustomerSummaryResponseDto monthly = new CustomerSummaryResponseDto();
        monthly.setAccountNumber(accountNumber);
        monthly.setCustomerName(customerName);
        monthly.setPeriode(formattedDate);
        monthly.setTotalExpense(totalExpense);
        monthly.setTotalIncome(totalIncome);

        return monthly;
    }

    @Override
    public CustomerSummaryResponseDto year(String accountNumber){
        boolean isAccountExist = repository.existsByAccountNumber(accountNumber);
        if (!isAccountExist) {
            throw new IllegalArgumentException("account number is not found!");
        }

        List<Transaction> transactions = repository.findByAccountNumber(accountNumber);
        int totalExpense = 0;
        int totalIncome = 0;
        String customerName = null;

        for (Transaction t : transactions) {
            if (t.getTransactionType().equalsIgnoreCase("debit")) {
                totalExpense += t.getAmount();
            }else if (t.getTransactionType().equalsIgnoreCase("kredit")) {
                totalIncome += t.getAmount();
            }
            if (customerName == null && t.getCustomerName() != null) {
                customerName = t.getCustomerName();
            }
        }

         // Ambil  tahun saat ini
        LocalDate currentDate = LocalDate.now();
        // Format  menjadi " YYYY"
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy");
        String formattedDate = currentDate.format(formatter);

        CustomerSummaryResponseDto year = new CustomerSummaryResponseDto();
        year.setAccountNumber(accountNumber);
        year.setCustomerName(customerName);
        year.setPeriode(formattedDate);
        year.setTotalExpense(totalExpense);
        year.setTotalIncome(totalIncome);

        return year;
    }


@Override
    public CustomerFinancialSummaryDto deviation(String accountNumber, LocalDateTime startDate, LocalDateTime endDate) {
        // periksa apakah endDate lebih kecil dari startDate
        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("Maaf, tanggal akhir tidak boleh lebih kecil dari tanggal awal");
        }
        List<Transaction> transactions = repository.findByAccountNumberAndTransactionDateBetween(accountNumber, startDate, endDate);

        int totalExpense = 0;
        int totalIncome = 0;
        String customerName = null;

        for (Transaction transaction : transactions) {
            if (transaction.getTransactionType().equalsIgnoreCase("debit")) {
                totalExpense += transaction.getAmount();
            } else if (transaction.getTransactionType().equalsIgnoreCase("kredit")) {
                totalIncome += transaction.getAmount();
            }
             // Mendapatkan nama pelanggan dari transaksi pertama dalam rentang waktu
                if (customerName == null && transaction.getCustomerName() != null) {
                    customerName = transaction.getCustomerName();
                }
        }
        //DEVIATION
        int deviation = totalIncome - totalExpense;
        String conclusion;
        if (deviation > 0) {
            conclusion = "good";
        } else if (deviation == 0) {
            conclusion = "warning";
        } else {
            conclusion = "danger";
        }

        CustomerFinancialSummaryDto summaryDto = new CustomerFinancialSummaryDto();

        summaryDto.setAccountNumber(accountNumber);
        summaryDto.setCustomerName(customerName); 
        summaryDto.setTotalExpense(totalExpense);
        summaryDto.setTotalIncome(totalIncome);
        summaryDto.setDeviation(deviation);
        summaryDto.setConclusion(conclusion);
        
        return summaryDto;
    }

        
@Override
public List<CustomerFinancialSummaryDto> viewDeviationAllCustomer(LocalDateTime startDate, LocalDateTime endDate) {
    if (endDate.isBefore(startDate)) {
        throw new IllegalArgumentException("Maaf, tanggal akhir tidak boleh lebih kecil dari tanggal awal");
    }    

    List<Transaction> transactions = repository.findByTransactionDateBetween(startDate, endDate);

    Map<String, CustomerFinancialSummaryDto> customerSummaries = new HashMap<>(); // Menggunakan map untuk menyimpan hasil per pelanggan

    for (Transaction transaction : transactions) {
        String accountNumber = transaction.getAccountNumber();
        if (!customerSummaries.containsKey(accountNumber)) {
            customerSummaries.put(accountNumber, new CustomerFinancialSummaryDto());
            customerSummaries.get(accountNumber).setAccountNumber(accountNumber);
            customerSummaries.get(accountNumber).setCustomerName(transaction.getCustomerName());
        }
        int totalExpense = customerSummaries.get(accountNumber).getTotalExpense();
        int totalIncome = customerSummaries.get(accountNumber).getTotalIncome();
        if (transaction.getTransactionType().equalsIgnoreCase("debit")) {
            totalExpense += transaction.getAmount();
        } else if (transaction.getTransactionType().equalsIgnoreCase("kredit")) {
            totalIncome += transaction.getAmount();
        }
        customerSummaries.get(accountNumber).setTotalExpense(totalExpense);
        customerSummaries.get(accountNumber).setTotalIncome(totalIncome);
    }

    List<CustomerFinancialSummaryDto> summaryList = new ArrayList<>(customerSummaries.values());
    for (CustomerFinancialSummaryDto summaryDto : summaryList) {
        // Menghitung deviation dan menentukan kesimpulan
        int deviation = summaryDto.getTotalIncome() - summaryDto.getTotalExpense();
        String conclusion = deviation > 0 ? "good" : (deviation == 0 ? "warning" : "danger");
        summaryDto.setDeviation(deviation);
        summaryDto.setConclusion(conclusion);
    }

    return summaryList;
    }
}
