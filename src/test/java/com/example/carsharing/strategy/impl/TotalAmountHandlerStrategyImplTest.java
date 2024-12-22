package com.example.carsharing.strategy.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.example.carsharing.model.Payment;
import com.example.carsharing.strategy.TotalAmountHandler;
import java.util.List;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TotalAmountHandlerStrategyImplTest {
    private List<TotalAmountHandler> handlers;
    @Mock
    private FineTotalAmountHandler fineHandler;
    @Mock
    private PaymentTotalAmountHandler paymentHandler;
    private TotalAmountHandlerStrategyImpl amountHandlerStrategy;

    @BeforeEach
    void setUp() {
        handlers = List.of(fineHandler, paymentHandler);
        amountHandlerStrategy = new TotalAmountHandlerStrategyImpl(handlers);
    }

    @Test
    void getToTalAmountHandler_fineHandler_ok() {
        when(fineHandler.isApplicable(any())).thenReturn(true);
        String result =
                amountHandlerStrategy.getToTalAmountHandler(Payment.Type.FINE)
                        .getClass().getSimpleName();
        int index = result.indexOf("$");
        String actual = result.substring(0, index);
        String expected = "FineTotalAmountHandler";
        assertEquals(expected, actual);
    }

    @Test
    void getToTalAmountHandler_paymentHandler_ok() {
        when(paymentHandler.isApplicable(any())).thenReturn(true);
        String result =
                amountHandlerStrategy.getToTalAmountHandler(Payment.Type.PAYMENT)
                        .getClass().getSimpleName();
        int index = result.indexOf("$");
        String actual = result.substring(0, index);
        String expected = "PaymentTotalAmountHandler";
        assertEquals(expected, actual);
    }

    @Test
    void getToTalAmountHandler_noImplementation_ok() {
        assertThrows(NoSuchElementException.class,
                () -> amountHandlerStrategy.getToTalAmountHandler(Payment.Type.FINE));
    }
}
