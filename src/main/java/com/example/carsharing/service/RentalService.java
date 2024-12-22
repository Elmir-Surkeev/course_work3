package com.example.carsharing.service;

import com.example.carsharing.model.Rental;
import com.example.carsharing.model.User;
import java.util.List;

public interface RentalService {
    Rental add(Rental requestRental);

    List<Rental> getByUserAndActiveness(User user, boolean isActive);

    Rental getById(Long id);

    Rental terminate(Long id);

    void checkRentalsForOverdueAndNotify();
}
