package com.redolf.rancard.Controllers;


import com.redolf.rancard.dtos.TransactionRequest;
import com.redolf.rancard.dtos.TransactionResponse;
import com.redolf.rancard.services.impls.TransactionServiceImpl;
import jakarta.validation.Valid;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class TransactionController {

    private final TransactionServiceImpl serviceImpl;
    public TransactionController(TransactionServiceImpl serviceImpl) {
        this.serviceImpl = serviceImpl;
    }

    @SneakyThrows
    @GetMapping("/transaction/{transactionId}")
    private ResponseEntity<?> findTransactionById(@PathVariable int transactionId){
        final TransactionResponse transaction = serviceImpl.findTransactionById(transactionId);
        return new ResponseEntity<>(transaction, HttpStatus.OK);
    }

    @GetMapping("/transactions")
    private ResponseEntity<?> getAllTransactions(){
        final List<TransactionResponse> transactions = serviceImpl.getAllTransactions();
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }

    @GetMapping("/transactions/page-size/{pageSize}")
    private ResponseEntity<?> getTransactionsWithPageSize(@PathVariable int pageSize){
        final List<TransactionResponse> transactions = serviceImpl.getTransactionsWithPageSize(pageSize);
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }

    @GetMapping("/transactions/off-set/{offSet}/page-size/{pageSize}")
    private ResponseEntity<?> getTransactionsWithOffSetAndPageSize(@PathVariable int offSet ,@PathVariable int pageSize){
        final List<TransactionResponse> transactions = serviceImpl.getTransactionsWithAndOffSetPageSize(offSet,pageSize);
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }

    @PostMapping("/transaction")
    private ResponseEntity<?> createTransaction(@Valid @RequestBody TransactionRequest request){
        final TransactionResponse transaction = serviceImpl.createTransaction(request);
        return new ResponseEntity<>(transaction, HttpStatus.CREATED);
    }

    @DeleteMapping("/transaction/{transactionId}")
    private ResponseEntity<?> deleteTransactionById(@PathVariable int transactionId){
        serviceImpl.deleteTransactionById(transactionId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/transaction/{transactionId}")
    private ResponseEntity<?> updateTransaction(@Valid @RequestBody TransactionRequest request, @PathVariable int transactionId){
        final TransactionResponse transaction = serviceImpl.updateTransaction(transactionId,request);
        return new ResponseEntity<>(transaction, HttpStatus.OK);
    }
}
