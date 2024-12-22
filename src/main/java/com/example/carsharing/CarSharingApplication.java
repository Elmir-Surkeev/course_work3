package com.example.carsharing;

import com.example.carsharing.service.impl.TelegramNotificationsService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@EnableScheduling
@SpringBootApplication
public class CarSharingApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context =
                SpringApplication.run(CarSharingApplication.class, args);
        registerTelegramBotNotificationService(context);
    }

    private static void registerTelegramBotNotificationService(
            ConfigurableApplicationContext context) {
        TelegramNotificationsService notificationsService =
                context.getBean(TelegramNotificationsService.class);

        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(notificationsService);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
