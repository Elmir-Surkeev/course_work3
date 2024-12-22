package com.example.carsharing.service;

import com.example.carsharing.model.Payment;
import com.example.carsharing.model.Rental;
import java.util.List;

public interface NotificationsService {
    void notify(String message, String chatId);

    void rentalToMessage(Rental rental);

    void overdueRentalsToMessage(List<Rental> rentalOverdueList, short remainingTime);

    void paymentToMessage(Payment payment);
}
