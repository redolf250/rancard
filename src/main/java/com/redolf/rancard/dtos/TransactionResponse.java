package com.redolf.rancard.dtos;

import lombok.*;

import java.util.Date;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionResponse {
    private int id;
    private String sender;
    private String receiver;
    private  double amount;
    private Date transactionDate;
}
