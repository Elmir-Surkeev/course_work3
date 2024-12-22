package com.example.carsharing.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.carsharing.model.Payment;
import com.example.carsharing.model.User;
import com.example.carsharing.repository.PaymentRepository;
import com.example.carsharing.service.NotificationsService;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PaymentServiceImplTest {
    @Mock
    private PaymentRepository paymentRepository;
    @Mock
    private NotificationsService notificationsService;
    @InjectMocks
    private PaymentServiceImpl paymentService;
    private Payment payment;

    @BeforeEach
    void setUp() {
        payment = new Payment();
        payment.setStatus(Payment.Status.PENDING);
        payment.setType(Payment.Type.PAYMENT);
        payment.setAmountToPay(BigDecimal.TEN);
    }

    @Test
    void add_validPayment_ok() {
        Long expected = 1L;
        when(paymentRepository.save(any(Payment.class))).thenReturn(new Payment(expected));
        Long actual = paymentService.add(payment).getId();
        assertEquals(expected, actual);
    }

    @Test
    void getByUser_validUser_ok() {
        User user = new User(1L);
        payment.setId(1L);
        when(paymentRepository.getByUser(any(User.class))).thenReturn(List.of(payment));
        List<Payment> actual = paymentService.getByUser(user);
        int expectedSize = 1;
        assertEquals(expectedSize, actual.size());
        assertEquals(payment.getId(), actual.get(0).getId());
    }

    @Test
    void setPaid_validSession_ok() {
        String sessionId = "session_id";
        when(paymentRepository.getBySessionId(sessionId)).thenReturn(payment);
        doNothing().when(notificationsService).paymentToMessage(payment);
        Payment.Status expected = Payment.Status.PAID;
        Payment actual = paymentService.setPaid(sessionId);
        assertEquals(expected, actual.getStatus());
        verify(notificationsService).paymentToMessage(payment);
    }
}
