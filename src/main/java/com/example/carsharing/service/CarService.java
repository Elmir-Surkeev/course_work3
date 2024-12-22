package com.example.carsharing.service;

import com.example.carsharing.model.Car;
import java.util.List;

public interface CarService {
    Car add(Car car);

    List<Car> getAll();

    Car getById(Long id);

    Car update(Long id, Car car);

    void delete(Long id);
}
