package com.example.carsharing.service.impl;

import com.example.carsharing.model.Payment;
import com.example.carsharing.model.User;
import com.example.carsharing.repository.PaymentRepository;
import com.example.carsharing.service.NotificationsService;
import com.example.carsharing.service.PaymentService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
@Service
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final NotificationsService notificationsService;

    @Override
    public Payment add(Payment payment) {
        return paymentRepository.save(payment);
    }

    @Override
    public List<Payment> getByUser(User user) {
        return paymentRepository.getByUser(user);
    }

    @Override
    @Transactional
    public Payment setPaid(String sessionId) {
        Payment payment = paymentRepository.getBySessionId(sessionId);
        if (payment.getStatus() != Payment.Status.PAID) {
            payment.setStatus(Payment.Status.PAID);
            notificationsService.paymentToMessage(payment);
        }
        return payment;
    }
}
