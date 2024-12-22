package com.example.carsharing.service.impl;

import com.example.carsharing.exception.AlreadyTerminatedRentalException;
import com.example.carsharing.exception.NotEnoughCarInventoryException;
import com.example.carsharing.model.Car;
import com.example.carsharing.model.Rental;
import com.example.carsharing.model.User;
import com.example.carsharing.repository.RentalRepository;
import com.example.carsharing.service.CarService;
import com.example.carsharing.service.NotificationsService;
import com.example.carsharing.service.RentalService;
import com.example.carsharing.service.UserService;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RentalServiceImpl implements RentalService {
    private static final int MIN_AMOUNT_TO_RENT = 1;
    private static final short RENT_TIME_LEFT = 6;
    private final RentalRepository rentalRepository;
    private final CarService carService;
    private final UserService userService;
    private final NotificationsService notificationsService;

    @Override
    public Rental add(Rental requestRental) {
        Long carId = requestRental.getCar().getId();
        Car car = carService.getById(carId);
        if (car.getInventory() < MIN_AMOUNT_TO_RENT) {
            throw new NotEnoughCarInventoryException("Can't rent car with id: " + carId
                    + ", not enough inventory: " + car.getInventory());
        }
        Rental rental = new Rental();
        rental.setRentalDate(LocalDateTime.now());
        rental.setReturnDate(requestRental.getReturnDate());
        rental.setCar(car);
        rental.setUser(userService.getById(requestRental.getUser().getId()));
        car.setInventory(car.getInventory() - 1);
        carService.update(car.getId(), car);
        notificationsService.rentalToMessage(rental);
        return rentalRepository.save(rental);
    }

    @Override
    public List<Rental> getByUserAndActiveness(User user, boolean isActive) {
        return isActive ? rentalRepository.findRentalByUserAndActualReturnDateIsNull(user) :
                rentalRepository.findRentalByUserAndActualReturnDateIsNotNull(user);
    }

    @Override
    public Rental getById(Long id) {
        return rentalRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("Can't get rental with id: " + id));
    }

    @Override
    public Rental terminate(Long id) {
        Rental rental = getById(id);
        if (rental.getActualReturnDate() != null) {
            throw new AlreadyTerminatedRentalException("Rental with id " + id + " already "
                    + "terminated");
        }
        rental.setActualReturnDate(LocalDateTime.now());
        rentalRepository.save(rental);
        Car car = rental.getCar();
        car.setInventory(car.getInventory() + 1);
        carService.update(car.getId(), car);
        return rental;
    }

    @Override
    public void checkRentalsForOverdueAndNotify() {
        List<Rental> rentalOverdueList = new ArrayList<>();
        for (Rental rental : rentalRepository.findAll()) {
            LocalDateTime returnDate = rental.getReturnDate().withSecond(0).withNano(0);
            LocalDateTime now = LocalDateTime.now().withSecond(0).withNano(0);
            if (now.plusHours(RENT_TIME_LEFT).equals(returnDate)
                    && rental.getActualReturnDate() == null) {
                rentalOverdueList.add(rental);
            }
        }
        if (!rentalOverdueList.isEmpty()) {
            notificationsService.overdueRentalsToMessage(rentalOverdueList, RENT_TIME_LEFT);
        }
    }
}
