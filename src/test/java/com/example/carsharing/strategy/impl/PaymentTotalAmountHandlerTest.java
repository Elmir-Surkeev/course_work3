package com.example.carsharing.strategy.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.carsharing.model.Car;
import com.example.carsharing.model.Payment;
import com.example.carsharing.model.Rental;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PaymentTotalAmountHandlerTest {
    private Car car;
    private PaymentTotalAmountHandler paymentHandler;

    @BeforeEach
    void setUp() {
        car = new Car();
        car.setDailyFee(BigDecimal.ONE);
        paymentHandler = new PaymentTotalAmountHandler();
    }

    @Test
    void getRentDays_oneDay_ok() {
        Rental rental = new Rental();
        rental.setRentalDate(LocalDateTime.of(2023, 8,3,10,30));
        rental.setReturnDate(LocalDateTime.of(2023, 8,3,15,30));
        BigDecimal expected = BigDecimal.ONE;
        BigDecimal actual = paymentHandler.getRentDays(rental);
        assertEquals(expected, actual);
    }

    @Test
    void getRentDays_twoDays_ok() {
        Rental rental = new Rental();
        rental.setRentalDate(LocalDateTime.of(2023, 8,3,10,30));
        rental.setReturnDate(LocalDateTime.of(2023, 8,4,15,30));
        BigDecimal expected = BigDecimal.valueOf(2L);
        BigDecimal actual = paymentHandler.getRentDays(rental);
        assertEquals(expected, actual);
    }

    @Test
    void getRentDays_oneWeek_ok() {
        Rental rental = new Rental();
        rental.setRentalDate(LocalDateTime.of(2023, 8,3,10,30));
        rental.setReturnDate(LocalDateTime.of(2023, 8,9,15,30));
        BigDecimal expected = BigDecimal.valueOf(7L);
        BigDecimal actual = paymentHandler.getRentDays(rental);
        assertEquals(expected, actual);
    }

    @Test
    void getTotalAmount_oneDayFiveRerDay_ok() {
        Rental rental = new Rental();
        rental.setRentalDate(LocalDateTime.of(2023, 8,3,10,30));
        rental.setReturnDate(LocalDateTime.of(2023, 8,3,15,30));
        BigDecimal expected = BigDecimal.valueOf(5L);
        BigDecimal actual = paymentHandler.getTotalAmount(rental, BigDecimal.valueOf(5L));
        assertEquals(expected, actual);
    }

    @Test
    void getTotalAmount_threeDayFiveRerDay_ok() {
        Rental rental = new Rental();
        rental.setRentalDate(LocalDateTime.of(2023, 8,3,10,30));
        rental.setReturnDate(LocalDateTime.of(2023, 8,5,15,30));
        BigDecimal expected = BigDecimal.valueOf(15L);
        BigDecimal actual = paymentHandler.getTotalAmount(rental, BigDecimal.valueOf(5L));
        assertEquals(expected, actual);
    }

    @Test
    void isApplicable_typePayment_ok() {
        boolean actual = paymentHandler.isApplicable(Payment.Type.PAYMENT);
        assertTrue(actual);
    }

    @Test
    void isApplicable_typeFine_ok() {
        boolean actual = paymentHandler.isApplicable(Payment.Type.FINE);
        assertFalse(actual);
    }
}
