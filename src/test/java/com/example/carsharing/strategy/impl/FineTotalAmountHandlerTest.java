package com.example.carsharing.strategy.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.carsharing.model.Payment;
import com.example.carsharing.model.Rental;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FineTotalAmountHandlerTest {
    private FineTotalAmountHandler fineHandler;

    @BeforeEach
    void setUp() {
        fineHandler = new FineTotalAmountHandler();
    }

    @Test
    void getOverdueDays_zeroOverdueDays_ok() {
        Rental rental = new Rental();
        rental.setRentalDate(LocalDateTime.of(2023, 8,3,10,30));
        rental.setReturnDate(LocalDateTime.of(2023, 8,3,15,30));
        rental.setActualReturnDate(LocalDateTime.of(2023, 8,3,15,30));
        BigDecimal actual = fineHandler.getTotalAmount(rental, BigDecimal.ONE);
        BigDecimal expected = BigDecimal.ONE;
        assertEquals(expected, actual);
    }

    @Test
    void getOverdueDays_oneOverdueDay_ok() {
        Rental rental = new Rental();
        rental.setRentalDate(LocalDateTime.of(2023, 8,3,10,30));
        rental.setReturnDate(LocalDateTime.of(2023, 8,3,15,30));
        rental.setActualReturnDate(LocalDateTime.of(2023, 8,4,15,30));
        BigDecimal actual = fineHandler.getTotalAmount(rental, BigDecimal.ONE);
        BigDecimal expected = BigDecimal.valueOf(2.25);
        assertEquals(expected, actual);
    }

    @Test
    void getOverdueDays_fiveOverdueDays_ok() {
        Rental rental = new Rental();
        rental.setRentalDate(LocalDateTime.of(2023, 8,3,10,30));
        rental.setReturnDate(LocalDateTime.of(2023, 8,3,15,30));
        rental.setActualReturnDate(LocalDateTime.of(2023, 8,8,15,30));
        BigDecimal actual = fineHandler.getTotalAmount(rental, BigDecimal.valueOf(5L));
        BigDecimal expected = BigDecimal.valueOf(36.25);
        assertEquals(expected, actual);
    }

    @Test
    void getOverdueDays_1OverdueDays_ok() {
        Rental rental = new Rental();
        rental.setRentalDate(LocalDateTime.of(2023, 8,3,10,30));
        rental.setReturnDate(LocalDateTime.of(2023, 8,3,15,30));
        rental.setActualReturnDate(LocalDateTime.of(2023, 8,13,15,30));
        BigDecimal actual = fineHandler.getTotalAmount(rental, BigDecimal.valueOf(7L));
        DecimalFormat f = new DecimalFormat("##.00");
        BigDecimal value = BigDecimal.valueOf(94.5);
        BigDecimal expected = value.setScale(2, BigDecimal.ROUND_HALF_UP);
        assertEquals(expected, actual);
    }

    @Test
    void isApplicable_fineType_ok() {
        boolean actual = fineHandler.isApplicable(Payment.Type.FINE);
        assertTrue(actual);
    }

    @Test
    void isApplicable_paymentType_ok() {
        boolean actual = fineHandler.isApplicable(Payment.Type.PAYMENT);
        assertFalse(actual);
    }
}
