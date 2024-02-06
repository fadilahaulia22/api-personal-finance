package com.uts.demo.service;

import java.util.List;

import com.uts.demo.dto.TransactionDto;
import com.uts.demo.dto.TransactionRequestDto;
import com.uts.demo.dto.TransactionResponseDto;


public interface TransactionServise {
    String create(TransactionDto dto);

    String remove(String id);
    
    List<TransactionResponseDto> view();

    boolean check(String accountNumber);

    String update(String id,TransactionRequestDto dto);

    // TransactionDto findById(String id);
}
