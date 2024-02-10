package com.redolf.rancard.services.impls;

import com.redolf.rancard.dtos.TransactionRequest;
import com.redolf.rancard.dtos.TransactionResponse;
import com.redolf.rancard.exceptions.InternalServerErrorException;
import com.redolf.rancard.exceptions.MethodArgumentException;
import com.redolf.rancard.exceptions.TransactionNotFoundException;
import com.redolf.rancard.models.Transaction;
import com.redolf.rancard.repositories.TransactionRepository;
import com.redolf.rancard.services.interfaces.TransactionService;
import jakarta.transaction.InvalidTransactionException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import lombok.SneakyThrows;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;


@Service
public class TransactionServiceImpl implements TransactionService<TransactionRequest, TransactionResponse> {

    private final TransactionRepository repository;

    private final ModelMapper mapper;


    private final Validator validator;

    public TransactionServiceImpl(TransactionRepository repository, ModelMapper mapper, Validator validator) {
        this.repository = repository;
        this.mapper = mapper;
        this.validator = validator;
    }


    /**
     * @param entity: The entity here refers to the request body of the request,
     *        which is {@link TransactionRequest}. The request is then mapped to
     *        actual transaction entity {@link Transaction} for data persistence.
     * @return {@link TransactionResponse} : The operation returns {@link Transaction} which is mapped to {@link TransactionResponse}
     */
    @SneakyThrows
    public TransactionResponse createTransaction(@Valid TransactionRequest entity) {
        Transaction transaction = Transaction.builder()
                .sender(entity.getSender())
                .receiver(entity.getReceiver())
                .amount(entity.getAmount())
                .transactionDate(new Date())
                .build();
        try{
            Set<ConstraintViolation<TransactionRequest>> violations = validator.validate(entity);
            if (!violations.isEmpty()) {
                throw new InvalidTransactionException("Oops! invalid data provided");
            }else{
                Transaction saveEntity = repository.save(transaction);
                return mapper.map(saveEntity, TransactionResponse.class);
            }
        }catch (RuntimeException e){
            throw new InternalServerErrorException("Oops! something went wrong, try again later.");
        }
    }


    /**
     * @param id: The id of the database record to updated.
     * @param entity: The new record for the entity {@link TransactionRequest} to updated.
     * @return {@link TransactionResponse} : The operation returns {@link Transaction}
     * which is mapped to {@link TransactionResponse}
     */
    @SneakyThrows
    public TransactionResponse updateTransaction(int id, @Valid TransactionRequest entity) {
        try {
            final Optional<Transaction> transaction = repository.findById(id);
            Set<ConstraintViolation<TransactionRequest>> violations = validator.validate(entity);
            if (!violations.isEmpty()) {
                throw new MethodArgumentException(null,null);
            }else{
                if (transaction.isPresent()){
                    final Transaction update = Transaction.builder()
                            .id(id)
                            .amount(entity.getAmount())
                            .sender(entity.getSender())
                            .receiver(entity.getReceiver())
                            .transactionDate(transaction.get().getTransactionDate())
                            .build();
                    final Transaction result = repository.save(update);
                    return mapTransaction(result);
                }else {
                    throw new TransactionNotFoundException("Oops! no such transaction with id " +id+" found");
                }
            }

        }catch (RuntimeException e){
            throw new InternalServerErrorException("Oops! something went wrong, try again later");
        }
    }


    /**
     * @param id: The id of the transaction to be removed from the database;
     * @return {@link TransactionResponse}: The {@link Transaction} object with the given id is
     *          returned if found and then mapped to {@link TransactionResponse} object otherwise
     *          it throws {@link TransactionNotFoundException} exception.
     */
    @SneakyThrows
    public TransactionResponse findTransactionById(int id) {
        try {
            final Optional<Transaction> transaction = repository.findById(id);
            if (transaction.isPresent()){
                return mapTransaction(transaction.get());
            }else {
                throw new TransactionNotFoundException("Oops! no such transaction with id " +id+" found");
            }
        }catch (RuntimeException e){
            throw new InternalServerErrorException("Oops! something went wrong, try again later.");
        }
    }


    /**
     * @param id: The id of the transaction to be removed from the database if found,
     *          otherwise throws {@link TransactionNotFoundException}
     */
    @SneakyThrows
    public void deleteTransactionById(int id) {
        try {
            final Optional<Transaction> transaction = repository.findById(id);
            if (transaction.isPresent()){
                repository.delete(transaction.get());
            }else {
                throw new TransactionNotFoundException("Oops! no such transaction with id " +id+" found");
            }
        }catch (RuntimeException e){
            throw new InternalServerErrorException("Oops! something went wrong, try again later.");
        }
    }

    /**
     * @param pageSize: The page size refers to the number of {@link Transaction} records to be retrieved.
     * @return : {@link List<Transaction>} which is mapped to {@link TransactionResponse}
     * using the map method in {@link java.util.stream.Stream} api by passing mapTransaction method to it,
     * return a list of {@link TransactionResponse}.
     */
    @SneakyThrows
    public List<TransactionResponse> getTransactionsWithPageSize(int pageSize) {
        try {
            return repository.findAll(Pageable.ofSize(pageSize)).stream().map(this::mapTransaction).toList();
        }catch (RuntimeException e){
            throw new InternalServerErrorException("Oops! something went wrong, try again later.");
        }
    }


    /**
     * @return : {@link List<Transaction>} which is mapped to {@link TransactionResponse}
     * using the map method in {@link java.util.stream.Stream} api by passing mapTransaction method to it,
     * return a list of {@link TransactionResponse}.
     */
    @SneakyThrows
    public List<TransactionResponse> getAllTransactions() {
        try {
            return repository.findAll().stream().map(this::mapTransaction).toList();
        }catch (RuntimeException e){
            throw new InternalServerErrorException("Oops! something went wrong, try again later.");
        }
    }


    /**
     * @param pageNumber: The page number indicates which specific page of content you are currently
     *                  viewing within a larger set of paginated data.
     * @param pageSize: The page size refers to the number of {@link Transaction} records to be retrieved.
     * @return : {@link List<Transaction>} which is mapped to {@link TransactionResponse}
     * using the map method in {@link java.util.stream.Stream} api by passing mapTransaction method to it,
     * return a list of {@link TransactionResponse} based on the parameters specified.
     */
    @SneakyThrows
    public List<TransactionResponse> getTransactionsWithOffSetAndPageSize(int pageNumber, int pageSize) {
        try {
            return repository.findAll(PageRequest.of(pageNumber,pageSize)).stream().map(this::mapTransaction).toList();
        }catch (Exception e){
            throw new InternalServerErrorException("Oops! something went wrong, try again later.");
        }
    }

    private TransactionResponse mapTransaction(Transaction transaction){
        return mapper.map(transaction, TransactionResponse.class);
    }
}
