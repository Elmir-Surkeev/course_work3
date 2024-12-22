package com.example.carsharing.service.impl;

import com.example.carsharing.model.Payment;
import com.example.carsharing.model.Rental;
import com.example.carsharing.service.NotificationsService;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Service
@RequiredArgsConstructor
public class TelegramNotificationsService extends TelegramLongPollingBot
        implements NotificationsService {

    @Value("${telegram.bot.token}")
    private String token;

    @Value("${telegram.chat.id}")
    private String chatId;

    @Override
    public String getBotUsername() {
        return "Car Sharing Notifications Bot";
    }

    @Override
    public String getBotToken() {
        return token;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            Message message = update.getMessage();
            if (message.getText().equalsIgnoreCase("/start")) {
                String tgUserId = message.getChatId().toString();
                String responseText = "Hello, " + message.getFrom().getFirstName() + "!";
                notify(responseText, tgUserId);
            }
        }
    }

    @Override
    public void notify(String message, String chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(message);
        sendMessage.enableHtml(true);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void rentalToMessage(Rental rental) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy");

        String message = "New rental!" + System.lineSeparator()
                + "Customer email: " + rental.getUser().getEmail() + System.lineSeparator()
                + "Rent car id: " + rental.getCar().getId() + System.lineSeparator()
                + "rental date: " + rental.getRentalDate().format(formatter)
                + ", return date: " + rental.getReturnDate().format(formatter);
        notify(message, chatId);
    }

    @Override
    public void overdueRentalsToMessage(List<Rental> rentalOverdueList, short remainingTime) {
        StringBuilder builder = new StringBuilder();
        builder.append("These cars rental will be overdue in ").append(remainingTime)
                .append(" hours:").append(System.lineSeparator());
        rentalOverdueList.forEach(r -> builder.append(System.lineSeparator())
                .append("Rental id: ").append(r.getId())
                .append(System.lineSeparator())
                .append("Car id: ").append(r.getCar().getId())
                .append(System.lineSeparator())
                .append("Brand and model: ").append(r.getCar().getBrand())
                .append(" ").append(r.getCar().getModel())
                .append(System.lineSeparator())
                .append("User email: ").append(r.getUser().getEmail())
                .append(System.lineSeparator()));
        notify(builder.toString(), chatId);
    }

    @Override
    public void paymentToMessage(Payment payment) {
        String message = "New successful payment with ID: "
                + payment.getId() + System.lineSeparator()
                + "Type: " + payment.getType() + System.lineSeparator()
                + "Amount: $" + (payment.getAmountToPay()
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.CEILING)) + System.lineSeparator()
                + "<a href='"
                + payment.getSessionUrl()
                + "'>Payment session</a>" + System.lineSeparator()
                + "Customer: " + payment.getRental().getUser().getEmail();
        notify(message, chatId);
    }
}
