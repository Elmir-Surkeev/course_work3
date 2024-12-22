package com.example.carsharing.service.impl;

import com.example.carsharing.model.Car;
import com.example.carsharing.repository.CarRepository;
import com.example.carsharing.service.CarService;
import java.util.List;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CarServiceImpl implements CarService {
    private final CarRepository carRepository;

    @Override
    public Car add(Car car) {
        return carRepository.save(car);
    }

    @Override
    public Car getById(Long id) {
        return carRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("Not found car with id: " + id));
    }

    @Override
    public List<Car> getAll() {
        return (List<Car>) carRepository.findAll();
    }

    @Override
    @Transactional
    public Car update(Long id, Car car) {
        if (car == null) {
            throw new IllegalArgumentException("Car cannot be null");
        }
        Car carFromDb = carRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("Not found car with id: " + id));
        carFromDb.setModel(car.getModel());
        carFromDb.setBrand(car.getBrand());
        carFromDb.setType(car.getType());
        carFromDb.setInventory(car.getInventory());
        carFromDb.setDailyFee(car.getDailyFee());
        return carFromDb;
    }

    @Override
    public void delete(Long id) {
        carRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("Not found car with id: " + id));
        carRepository.deleteById(id);
    }
}
