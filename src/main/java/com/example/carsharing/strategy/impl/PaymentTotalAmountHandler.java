package com.example.carsharing.strategy.impl;

import com.example.carsharing.model.Payment;
import com.example.carsharing.model.Rental;
import com.example.carsharing.strategy.TotalAmountHandler;
import java.math.BigDecimal;
import org.springframework.stereotype.Service;

@Service
public class PaymentTotalAmountHandler implements TotalAmountHandler {

    @Override
    public BigDecimal getTotalAmount(Rental rental, BigDecimal dailyFee) {
        BigDecimal rentDays = getRentDays(rental);
        return rentDays.multiply(dailyFee);
    }

    @Override
    public boolean isApplicable(Payment.Type type) {
        return type == Payment.Type.PAYMENT;
    }

}
