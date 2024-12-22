package com.example.carsharing.dto.response;

import com.example.carsharing.model.Payment;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class PaymentResponseDto {
    private Long id;
    private Payment.Status status;
    private Payment.Type type;
    private String sessionUrl;
    private String sessionId;
    private BigDecimal amountToPay;
}
