package com.example.carsharing.strategy;

import com.example.carsharing.model.Payment;

public interface TotalAmountHandlerStrategy {
    TotalAmountHandler getToTalAmountHandler(Payment.Type type);
}
