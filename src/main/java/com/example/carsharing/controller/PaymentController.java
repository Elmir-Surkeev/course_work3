package com.example.carsharing.controller;

import com.example.carsharing.dto.request.PaymentRequestDto;
import com.example.carsharing.dto.response.PaymentResponseDto;
import com.example.carsharing.model.Car;
import com.example.carsharing.model.Payment;
import com.example.carsharing.model.Rental;
import com.example.carsharing.model.User;
import com.example.carsharing.payment.PaymentProvider;
import com.example.carsharing.service.PaymentService;
import com.example.carsharing.service.RentalService;
import com.example.carsharing.service.UserService;
import com.example.carsharing.service.mapper.PaymentMapper;
import com.example.carsharing.strategy.TotalAmountHandlerStrategy;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/payments")
public class PaymentController {
    private PaymentProvider paymentProvider;
    private RentalService rentalService;
    private PaymentService paymentService;
    private TotalAmountHandlerStrategy strategy;
    private PaymentMapper paymentMapper;
    private UserService userService;

    @GetMapping("/success")
    public String successPayment(@RequestParam("session_id") String sessionId) {
        if (paymentProvider.isSessionPaid(sessionId)) {
            paymentService.setPaid(sessionId);
        }
        return "Successful payment";
    }

    @GetMapping("/cancel")
    public String cancelPayment() {
        return "Something went wrong";
    }

    @PostMapping
    public PaymentResponseDto createSession(@RequestBody PaymentRequestDto paymentRequestDto)
            throws StripeException {
        Rental rental = rentalService.getById(paymentRequestDto.getRentalId());
        Payment payment = new Payment();
        payment.setRental(rental);
        payment.setStatus(Payment.Status.PENDING);
        payment.setType(paymentRequestDto.getType());
        Car car = rental.getCar();
        payment.setAmountToPay(strategy.getToTalAmountHandler(payment.getType())
                .getTotalAmount(rental, car.getDailyFee()));
        Session session = paymentProvider.createSession(payment, car);
        payment.setSessionId(session.getId());
        payment.setSessionUrl(session.getUrl());
        paymentService.add(payment);
        return paymentMapper.mapToDto(payment);
    }

    @GetMapping
    public List<PaymentResponseDto> getByUserId(@RequestParam("user_id") Long userId) {
        return paymentService.getByUser(userService.getById(userId)).stream()
                .map(paymentMapper::mapToDto)
                .toList();
    }

    @GetMapping("/my")
    public List<PaymentResponseDto> getPaymentsForCurrentUser(Authentication authentication) {
        User user = userService.getByEmail(authentication.getName()).get();
        return paymentService.getByUser(user).stream()
                .map(paymentMapper::mapToDto)
                .toList();
    }
}
