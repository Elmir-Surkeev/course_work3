package com.example.carsharing.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.carsharing.model.Car;
import com.example.carsharing.repository.CarRepository;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class CarServiceImplTest {
    private static final Long CAR_ID = 1L;
    private static final String CAR_MODEL = "Toyota Camry";
    private static final String NEW_MODEL = "New Model";
    private static final String CAR_BRAND = "Toyota";
    private static final String NEW_BRAND = "New Brand";
    private static final Car.Type CAR_TYPE = Car.Type.SEDAN;
    private static final Car.Type NEW_TYPE = Car.Type.UNIVERSAL;
    private static final int CAR_INVENTORY = 10;
    private static final int NEW_INVENTORY = 20;
    private static final BigDecimal CAR_DAILY_FEE = BigDecimal.valueOf(50.0);
    private static final BigDecimal NEW_DAILY_FEE = BigDecimal.valueOf(75.0);

    @Mock
    private CarRepository carRepository;

    @InjectMocks
    private CarServiceImpl carService;
    private Car car;

    @BeforeEach
    void setup() {
        car = new Car();
        car.setId(CAR_ID);
        car.setModel(CAR_MODEL);
        car.setBrand(CAR_BRAND);
        car.setType(CAR_TYPE);
        car.setInventory(CAR_INVENTORY);
        car.setDailyFee(CAR_DAILY_FEE);
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void add_ok() {
        when(carRepository.save(any(Car.class))).thenReturn(car);
        Car savedCar = carService.add(car);
        assertNotNull(savedCar);
        assertEquals(CAR_ID, savedCar.getId());
        assertEquals(CAR_MODEL, savedCar.getModel());
        assertEquals(CAR_BRAND, savedCar.getBrand());
        assertEquals(CAR_TYPE, savedCar.getType());
        assertEquals(CAR_INVENTORY, savedCar.getInventory());
        assertEquals(CAR_DAILY_FEE, savedCar.getDailyFee());
        verify(carRepository).save(car);
    }

    @Test
    void getAll_ok() {
        List<Car> carList = new ArrayList<>();
        carList.add(car);
        when(carRepository.findAll()).thenReturn(carList);
        List<Car> retrievedCars = carService.getAll();
        assertNotNull(retrievedCars);
        assertEquals(1, retrievedCars.size());
        assertEquals(car, retrievedCars.get(0));
        verify(carRepository).findAll();
    }

    @Test
    void getById_ok() {
        when(carRepository.findById(CAR_ID)).thenReturn(Optional.of(car));
        Car retrievedCar = carService.getById(CAR_ID);
        assertNotNull(retrievedCar);
        assertEquals(CAR_ID, retrievedCar.getId());
        assertEquals(CAR_MODEL, retrievedCar.getModel());
        assertEquals(CAR_BRAND, retrievedCar.getBrand());
        assertEquals(CAR_TYPE, retrievedCar.getType());
        assertEquals(CAR_INVENTORY, retrievedCar.getInventory());
        assertEquals(CAR_DAILY_FEE, retrievedCar.getDailyFee());
        verify(carRepository).findById(CAR_ID);
    }

    @Test
    void update_ok() {
        when(carRepository.findById(CAR_ID)).thenReturn(Optional.of(car));
        Car newCar = new Car();
        newCar.setId(CAR_ID);
        newCar.setModel(NEW_MODEL);
        newCar.setBrand(NEW_BRAND);
        newCar.setType(NEW_TYPE);
        newCar.setInventory(NEW_INVENTORY);
        newCar.setDailyFee(NEW_DAILY_FEE);
        when(carRepository.save(any(Car.class))).thenReturn(newCar);
        Car result = carService.update(CAR_ID, newCar);
        assertNotNull(result);
        assertEquals(newCar.getModel(), result.getModel());
        assertEquals(newCar.getBrand(), result.getBrand());
        assertEquals(newCar.getType(), result.getType());
        assertEquals(newCar.getInventory(), result.getInventory());
        assertEquals(newCar.getDailyFee(), result.getDailyFee());
        verify(carRepository).findById(CAR_ID);
    }

    @Test
    void delete_ok() {
        when(carRepository.findById(CAR_ID)).thenReturn(Optional.of(car));
        carService.delete(CAR_ID);
        verify(carRepository).deleteById(CAR_ID);
    }

    @Test
    void getById_carDoesNotExist_notOk() {
        when(carRepository.findById(CAR_ID)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> carService.getById(CAR_ID));
        verify(carRepository).findById(CAR_ID);
    }

    @Test
    void update_carDoesNotExist_notOk() {
        when(carRepository.findById(CAR_ID)).thenReturn(Optional.empty());
        Car updatedCar = new Car();
        updatedCar.setId(CAR_ID);
        assertThrows(NoSuchElementException.class, () -> carService.update(CAR_ID, updatedCar));
        verify(carRepository).findById(CAR_ID);
        verify(carRepository, never()).save(any(Car.class));
    }

    @Test
    void update_newCarIsNull_notOk() {
        assertThrows(IllegalArgumentException.class,
                () -> carService.update(CAR_ID,null));
        verify(carRepository, never()).save(any(Car.class));
    }

    @Test
    void delete_carDoesNotExist_notOk() {
        when(carRepository.findById(CAR_ID)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> carService.delete(CAR_ID));
        verify(carRepository).findById(CAR_ID);
        verify(carRepository, never()).deleteById(anyLong());
    }
}
