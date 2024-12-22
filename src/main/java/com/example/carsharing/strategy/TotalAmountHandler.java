package com.example.carsharing.strategy;

import static java.time.temporal.ChronoUnit.DAYS;

import com.example.carsharing.model.Payment;
import com.example.carsharing.model.Rental;
import java.math.BigDecimal;

public interface TotalAmountHandler {
    BigDecimal getTotalAmount(Rental rental, BigDecimal dailyFee);

    boolean isApplicable(Payment.Type type);

    default BigDecimal getRentDays(Rental rental) {
        return BigDecimal.valueOf(DAYS.between(rental.getRentalDate(), rental.getReturnDate()))
                .add(BigDecimal.ONE);
    }
}
