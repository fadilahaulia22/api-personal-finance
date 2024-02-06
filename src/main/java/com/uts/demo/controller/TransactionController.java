package com.uts.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    // @GetMapping("/find-by-id/{id}")
    // public TransactionDto findById(@PathVariable String id){
    //     return service.findById(id);
    // }

}

