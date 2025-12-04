package com.project2.secondProject.dto;

import lombok.Data;

@Data
public class PaymentRequestDTO {
    private long amount;
    private String currency;
}
