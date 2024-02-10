package com.redolf.rancard.services.impls;

import com.redolf.rancard.RancardApplication;
import com.redolf.rancard.dtos.TransactionRequest;
import com.redolf.rancard.dtos.TransactionResponse;
import com.redolf.rancard.exceptions.InternalServerErrorException;
import com.redolf.rancard.exceptions.TransactionNotFoundException;
import com.redolf.rancard.models.Transaction;
import com.redolf.rancard.repositories.TransactionRepository;
import jakarta.transaction.InvalidTransactionException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.OngoingStubbing;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest(classes = RancardApplication.class)
class TransactionServiceImplTest {

    @Autowired
    private TransactionServiceImpl serviceImpl;

    @Autowired
    public ModelMapper mapper;


    @MockBean
    public TransactionRepository repository;

    static List<Transaction> transactionStream;
    @BeforeAll
    static void setUp() {
         transactionStream = Stream.of(
                new Transaction(1, "Emelia", "Clark", 20, new Date()),
                new Transaction(2, "John", "Snow", 30, new Date()),
                new Transaction(3, "Emily", "Sacky", 40, new Date()),
                 new Transaction(1, "Emelia", "Clark", 20, new Date()),
                 new Transaction(2, "John", "Snow", 30, new Date()),
                 new Transaction(3, "Emily", "Sacky", 40, new Date()),
                 new Transaction(1, "Emelia", "Clark", 20, new Date()),
                 new Transaction(2, "John", "Snow", 30, new Date()),
                 new Transaction(3, "Emily", "Sacky", 40, new Date())
        ).toList();
    }

    @Test
    void createTransactionWithValidDetails() {
        Transaction transaction = Transaction.builder()
                .amount(20)
                .sender("Emelia Clark")
                .receiver("John Snow")
                .transactionDate(new Date())
                .build();
        when(repository.save(any(Transaction.class))).thenReturn(transaction);
        TransactionResponse actualResponse = serviceImpl.createTransaction(mapper.map(transaction, TransactionRequest.class));
        assertNotNull(actualResponse);
        verify(repository, times(1)).save(any(Transaction.class));
    }

//    @Test
//    void createTransactionWithSomeNullFields() {
//        Transaction transaction = Transaction.builder()
//                .amount(0)
//                .sender("")
//                .receiver("John Snow")
//                .transactionDate(new Date())
//                .build();
//        when(repository.save(any(Transaction.class))).thenReturn(transaction);
//        assertThrows(InvalidTransactionException.class, () -> serviceImpl.createTransaction(mapper.map(transaction,TransactionRequest.class)));
//        verify(repository, times(1)).save(any(Transaction.class));
//    }
//
//    @Test
//    void updateTransaction() {
//        Transaction transaction = Transaction.builder()
//                .id(45)
//                .amount(50)
//                .sender("Emilia")
//                .receiver("John Snow")
//                .transactionDate(new Date())
//                .build();
//        when(repository.findById(45)).thenReturn(Optional.of(transaction));
//
//        Transaction updatedTransaction = Transaction.builder()
//                .id(45)
//                .amount(46000)
//                .sender("Emilia")
//                .receiver("John Snow")
//                .transactionDate(transaction.getTransactionDate())
//                .build();
//        when(repository.save(any(Transaction.class))).thenReturn(updatedTransaction);
//
//        TransactionResponse expectedResponse = TransactionResponse.builder()
//                .id(45)
//                .amount(46000)
//                .sender("Emilia")
//                .receiver("John Snow")
//                .transactionDate(new Date())
//                .build();
//        when(mapTransaction(updatedTransaction)).thenReturn(expectedResponse);
//
//        TransactionResponse actualResponse = serviceImpl.updateTransaction(45, mapper.map(updatedTransaction,TransactionRequest.class));
//
//        verify(repository, times(1)).findById(45);
//
//        verify(repository, times(1)).save(any(Transaction.class));
//
//        assertNotNull(actualResponse);
//    }

    @Test
    void updateTransactionWhenNotFound() {
        when(repository.findById(130)).thenReturn(Optional.empty());
        assertThrows(TransactionNotFoundException.class, () -> serviceImpl.findTransactionById(130));
        verify(repository, times(1)).findById(130);
    }

    @Test
    void updateTransactionWithSomeNullFields(){
        Transaction transaction = Transaction.builder()
                .id(4555)
                .amount(0)
                .sender("")
                .receiver("John Snow")
                .transactionDate(new Date())
                .build();
        when(repository.findById(4555)).thenReturn(Optional.ofNullable(transaction));
        assertThrows(InternalServerErrorException.class, () -> serviceImpl.updateTransaction(4555, mapper.map(transaction, TransactionRequest.class)));
        verify(repository, times(1)).findById(4555);
    }

    @Test
    void findTransactionById() {
        Transaction transaction = Transaction.builder()
                .amount(20)
                .sender("Emelia Clark")
                .receiver("John Snow")
                .transactionDate(new Date())
                .build();
        when(repository.save(any(Transaction.class))).thenReturn(transaction);

        TransactionResponse actualResponse = serviceImpl.createTransaction(mapper.map(transaction, TransactionRequest.class));

        verify(repository, times(1)).save(any(Transaction.class));
        assertNotNull(actualResponse);
    }

    @Test
    public void findTransactionByIdWhenNotFound() {
        when(repository.findById(1245)).thenReturn(Optional.empty());
        assertThrows(TransactionNotFoundException.class, () -> serviceImpl.findTransactionById(1245));
        verify(repository, times(1)).findById(1245);
    }

    @Test
    void deleteTransactionById() {
        Transaction transaction = Transaction.builder()
                .amount(20)
                .sender("Emelia Clark")
                .receiver("John Snow")
                .transactionDate(new Date())
                .build();
        when(repository.findById(12345)).thenReturn(Optional.of(transaction));
        serviceImpl.deleteTransactionById(12345);
        verify(repository, times(1)).delete(transaction);
    }

    @Test
    public void deleteTransactionByIdWhenNotFound() {
        when(repository.findById(12345)).thenReturn(Optional.empty());
        assertThrows(TransactionNotFoundException.class, () -> serviceImpl.deleteTransactionById(12345));
        verify(repository, times(1)).findById(12345);
    }

    @Test
    public void deleteTransactionByIdFailure() {
        int transactionId = 123;
        when(repository.findById(transactionId)).thenThrow(new RuntimeException());
        assertThrows(InternalServerErrorException.class, () -> serviceImpl.deleteTransactionById(transactionId));
        verify(repository, times(1)).findById(transactionId);
    }

    @Test
    void getTransactionsWithPageSize() {
        List<Transaction> transactions = transactionStream.stream().skip(3).toList();
        List<TransactionResponse> expectedResponses = transactions.stream()
                .map(this::mapTransaction)
                .toList();
        Page<Transaction> transactionPage = new PageImpl<>(transactions);
        when(repository.findAll(any(Pageable.class))).thenReturn(transactionPage);
        List<TransactionResponse> actualResponses = serviceImpl.getTransactionsWithPageSize(5);
        assertEquals(expectedResponses.size(), actualResponses.size());
        verify(repository, times(1)).findAll(Pageable.ofSize(5));
    }

    @Test
    void getAllTransactions() {
        when(repository.findAll()).thenReturn(transactionStream);
        assertEquals(transactionStream.size(),serviceImpl.getAllTransactions().size());
        verify(repository, times(1)).findAll();
    }

    @Test
    void getTransactionsWithAndOffSetPageSize() {
        List<Transaction> transactions = transactionStream.stream().skip(5).toList();
        List<TransactionResponse> expectedResponses = transactions.stream()
                .map(this::mapTransaction)
                .toList();
        Page<Transaction> transactionPage = new PageImpl<>(transactions);
        when(repository.findAll(any(PageRequest.class))).thenReturn(transactionPage);
        List<TransactionResponse> actualResponses = serviceImpl.getTransactionsWithAndOffSetPageSize(0,3);
        assertEquals(expectedResponses.size(), actualResponses.size());
        verify(repository, times(1)).findAll(PageRequest.of(0,3));
    }

    private TransactionResponse mapTransaction(Transaction transaction){
        return mapper.map(transaction, TransactionResponse.class);
    }
}