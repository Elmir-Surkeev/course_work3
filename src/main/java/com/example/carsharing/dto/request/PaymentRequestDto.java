package com.example.carsharing.dto.request;

import com.example.carsharing.model.Payment;
import lombok.Data;

@Data
public class PaymentRequestDto {
    private Long rentalId;
    private Payment.Type type;
}
