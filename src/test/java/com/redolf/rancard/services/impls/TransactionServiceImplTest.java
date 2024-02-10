package com.redolf.rancard.services.impls;

import com.redolf.rancard.RancardApplication;
import com.redolf.rancard.dtos.TransactionRequest;
import com.redolf.rancard.dtos.TransactionResponse;
import com.redolf.rancard.exceptions.InternalServerErrorException;
import com.redolf.rancard.exceptions.TransactionNotFoundException;
import com.redolf.rancard.models.Transaction;
import com.redolf.rancard.repositories.TransactionRepository;
import jakarta.transaction.InvalidTransactionException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.apache.catalina.mapper.Mapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest(classes = RancardApplication.class)
class TransactionServiceImplTest extends AbstractContainerBaseTest{

    @Autowired
    private TransactionServiceImpl serviceImpl;

    @MockBean
    public TransactionRepository repository;

    @Autowired
    public Validator validator;

    @Autowired
    public ModelMapper mapper;

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

    @AfterEach
    void tearDown() {
        System.out.println(MY_SQL_CONTAINER.getPassword());
        System.out.println(MY_SQL_CONTAINER.getDatabaseName());
        System.out.println(MY_SQL_CONTAINER.getExposedPorts());
        System.out.println(MY_SQL_CONTAINER.getUsername());
        System.out.println(MY_SQL_CONTAINER.getJdbcUrl());
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

    @Test
    void createTransactionWithSomeIncorrectData() {
        Transaction transaction = Transaction.builder()
                .amount(-1)
                .sender(null)
                .receiver("John Snow")
                .transactionDate(new Date())
                .build();
        when(repository.save(transaction)).thenThrow(RuntimeException.class);
        assertThrows(InvalidTransactionException.class,()-> serviceImpl.createTransaction(mapper.map(transaction,TransactionRequest.class)));
    }

    @Test
    void updateTransaction() {
    }

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
    void getTransactionsWithInvalidPageSize() {
        assertThrows(InternalServerErrorException.class,()->serviceImpl.getTransactionsWithPageSize(-5));
    }

    @Test
    void getAllTransactions() {
        when(repository.findAll()).thenReturn(transactionStream);
        assertEquals(transactionStream.size(),serviceImpl.getAllTransactions().size());
        verify(repository, times(1)).findAll();
    }

    @Test
    void getTransactionsWithOffSetAndPageSize() {
        List<Transaction> transactions = transactionStream.stream().skip(5).toList();
        List<TransactionResponse> expectedResponses = transactions.stream()
                .map(this::mapTransaction)
                .toList();
        Page<Transaction> transactionPage = new PageImpl<>(transactions);
        when(repository.findAll(any(PageRequest.class))).thenReturn(transactionPage);
        List<TransactionResponse> actualResponses = serviceImpl.getTransactionsWithOffSetAndPageSize(0,3);
        assertEquals(expectedResponses.size(), actualResponses.size());
        verify(repository, times(1)).findAll(PageRequest.of(0,3));
    }

    @Test
    void getTransactionsWithInvalidOffSetAndPageSize() {
        assertThrows(InternalServerErrorException.class,()->serviceImpl.getTransactionsWithOffSetAndPageSize(-1,-5));
    }

    private TransactionResponse mapTransaction(Transaction transaction){
        return mapper.map(transaction, TransactionResponse.class);
    }
}