package com.uts.demo.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.uts.demo.dto.CustomerFinancialSummaryDto;
import com.uts.demo.dto.CustomerSummaryResponseDto;
import com.uts.demo.dto.TransactionDto;
import com.uts.demo.dto.TransactionRequestDto;
import com.uts.demo.service.TransactionServise;
import com.uts.demo.dto.TransactionResponseDto;


import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("personal-transaction")
public class TransactionController {
    
    @Autowired
    TransactionServise service;

    @Operation(summary = "add data",description = "you can add more data")
    @PostMapping("/add")
    public String create(@RequestBody TransactionDto dto){
        return service.create(dto);
    }

    @Operation(summary = "remove data",description = "if u want to delete, you can delete it")
    @DeleteMapping("/remove/{id}")
    public String remove(@PathVariable String id){
        return service.remove(id);
    } 

    @Operation(summary = "update data")
    @PutMapping("/update/{id}")
    public String update(@PathVariable String id ,@RequestBody TransactionRequestDto dto){
        return service.update(id, dto);
    }

    @Operation(summary = "view",description = "view all data list transaction")
    @GetMapping("/view")
    public List<TransactionResponseDto> view(){
        return service.view();
    }

    @Operation(summary = "Get total expenses and income for a customer within a specified time range")
    @GetMapping("/personal-expences-income")
    public ResponseEntity<?> getCustomerFinancialSummary(
        @RequestParam String accountNumber,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

            // Konversi LocalDate ke LocalDateTime dengan waktu awal hari
            LocalDateTime startDateTime = startDate.atStartOfDay();
            LocalDateTime endDateTime = endDate.atTime(23, 59, 59); // Waktu akhir hari
       
            try {
                // Panggil metode getCustomerFinancialSummary dari service
                CustomerFinancialSummaryDto summaryDto = service.getCustomerFinancialSummary(accountNumber, startDateTime, endDateTime);
                return ResponseEntity.ok(summaryDto);
            } catch (IllegalArgumentException e) {
                // pesan kesalahan jika endDate lebih kecil dari startDate dan kirimkan respons bad request dengan pesan kesalahan
                return ResponseEntity.badRequest().body(e.getMessage());
            }   
    }

    @Operation(summary = "Get total expenses and income for a customer within a day")
    @GetMapping("/personal-expences-income/daily")
    public ResponseEntity<?>  responseDto(@RequestParam String accountNumber) {

        try {
            // Panggil metode getCustomerFinancialSummary dari service
            CustomerSummaryResponseDto dto = service.responseDto(accountNumber);
            return ResponseEntity.ok(dto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }   
    }

    @Operation(summary = "Get total expenses and income for a customer within a monthly")
    @GetMapping("/personal-expences-income/monthly")
    public ResponseEntity<?>  montly(@RequestParam String accountNumber) {

        try {
            // Panggil metode getCustomerFinancialSummary dari service
            CustomerSummaryResponseDto dto = service.montly(accountNumber);
            return ResponseEntity.ok(dto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }   
    }

    @Operation(summary = "Get total expenses and income for a customer within a year")
    @GetMapping("/personal-expences-income/annual")
    public ResponseEntity<?>  year(@RequestParam String accountNumber) {

        try {
            // Panggil metode getCustomerFinancialSummary dari service
            CustomerSummaryResponseDto dto = service.year(accountNumber);
            return ResponseEntity.ok(dto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }   
    }


    @Operation(summary = "personal deviation")
    @GetMapping("/personal-defiation")
    public ResponseEntity<?> deviation(
        @RequestParam String accountNumber,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

            // Konversi LocalDate ke LocalDateTime dengan waktu awal hari
            LocalDateTime startDateTime = startDate.atStartOfDay();
            LocalDateTime endDateTime = endDate.atTime(23, 59, 59); // Waktu akhir hari
       
            try {
                // Panggil metode getCustomerFinancialSummary dari service
                CustomerFinancialSummaryDto summaryDto = service.deviation(accountNumber, startDateTime, endDateTime);
                return ResponseEntity.ok(summaryDto);
            } catch (IllegalArgumentException e) {
                // pesan kesalahan jika endDate lebih kecil dari startDate dan kirimkan respons bad request dengan pesan kesalahan
                return ResponseEntity.badRequest().body(e.getMessage());
            }  
    } 
    
    @Operation(summary = "personal deviation all customers")
    @GetMapping("/personal-defiation/all-customers")
    public ResponseEntity<List<CustomerFinancialSummaryDto>> viewDeviationAllCustomer(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

    // Konversi LocalDate ke LocalDateTime dengan waktu awal hari
    LocalDateTime startDateTime = startDate.atStartOfDay();
    LocalDateTime endDateTime = endDate.atTime(23, 59, 59); // Waktu akhir hari

    List<CustomerFinancialSummaryDto> deviationList = service.viewDeviationAllCustomer(startDateTime, endDateTime);

        return new ResponseEntity<>(deviationList, HttpStatus.OK);
    }
}

