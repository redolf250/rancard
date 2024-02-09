package com.redolf.rancard.dtos;

import jakarta.persistence.Entity;
import lombok.*;

import java.util.Date;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDTO {
    private int id;
    private String sender;
    private String address;
    private  double amount;
    private Date transactionDate;
}
