package com.redolf.rancard.dtos;

import jakarta.validation.constraints.*;
import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionRequest {
    @NotEmpty(message = "Sender field can't be empty")
    private String sender;
    @NotEmpty(message = "Receiver field can't be empty")
    private String receiver;
    @DecimalMin(message = "Amount field should be greater than or equal to 1", value = "1.0")
    private  double amount;
}
