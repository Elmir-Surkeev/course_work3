package com.example.carsharing.strategy.impl;

import com.example.carsharing.model.Payment;
import com.example.carsharing.strategy.TotalAmountHandler;
import com.example.carsharing.strategy.TotalAmountHandlerStrategy;
import java.util.List;
import java.util.NoSuchElementException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class TotalAmountHandlerStrategyImpl implements TotalAmountHandlerStrategy {
    private List<TotalAmountHandler> amountHandlers;

    @Override
    public TotalAmountHandler getToTalAmountHandler(Payment.Type type) {
        return amountHandlers.stream()
                .filter(handler -> handler.isApplicable(type))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Can't find handler for payment "
                        + "type: " + type.name()));
    }
}
