package com.example.carsharing.service.impl;

import com.example.carsharing.service.RentalScheduledService;
import com.example.carsharing.service.RentalService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RentalScheduledServiceImpl implements RentalScheduledService {
    private final RentalService rentalService;

    @Async
    @Scheduled(cron = "0 * * ? * *")
    @Override
    public void checkRentalsForOverdueAndNotify() {
        rentalService.checkRentalsForOverdueAndNotify();
    }
}
