package com.example.carsharing.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.carsharing.dto.request.CarRequestDto;
import com.example.carsharing.dto.response.CarResponseDto;
import com.example.carsharing.model.Car;
import com.example.carsharing.service.CarService;
import com.example.carsharing.service.mapper.CarMapper;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CarControllerTest {
    private static final Long CAR_ID = 1L;
    @Mock
    private CarService carService;
    @Mock
    private CarMapper carMapper;
    @InjectMocks
    private CarController carController;
    private CarRequestDto carRequestDto;
    private CarResponseDto carResponseDto;
    private Car car;

    @BeforeEach
    void setUp() {
        carRequestDto = new CarRequestDto();
        carRequestDto.setBrand("BMW");
        carRequestDto.setModel("M3");
        carRequestDto.setType(Car.Type.SEDAN);
        carRequestDto.setInventory(5);
        carRequestDto.setDailyFee(BigDecimal.TEN);
        carResponseDto = new CarResponseDto();
        carResponseDto.setId(CAR_ID);
        carResponseDto.setBrand(carRequestDto.getBrand());
        carResponseDto.setModel(carRequestDto.getModel());
        carResponseDto.setType(carRequestDto.getType());
        carResponseDto.setInventory(carRequestDto.getInventory());
        carResponseDto.setDailyFee(carRequestDto.getDailyFee());
        car = new Car();
        car.setBrand(carResponseDto.getBrand());
        car.setModel(carResponseDto.getModel());
        car.setType(carResponseDto.getType());
        car.setInventory(carResponseDto.getInventory());
        car.setDailyFee(carResponseDto.getDailyFee());
    }

    @Test
    void add_validCarRequestDto_ok() {
        when(carMapper.mapToEntity(any(CarRequestDto.class))).thenReturn(car);
        car.setId(CAR_ID);
        when(carService.add(any(Car.class))).thenReturn(car);
        when(carMapper.mapToDto(any(Car.class))).thenReturn(carResponseDto);
        CarResponseDto actual = carController.add(carRequestDto);
        assertEquals(carResponseDto, actual);
    }

    @Test
    void getAll_notEmptyDB_ok() {
        car.setId(CAR_ID);
        when(carService.getAll()).thenReturn(List.of(car));
        when(carMapper.mapToDto(any(Car.class))).thenReturn(carResponseDto);
        List<CarResponseDto> expected = List.of(carResponseDto);
        List<CarResponseDto> actual = carController.getAll();
        assertEquals(expected, actual);
    }

    @Test
    void getAll_emptyDb_ok() {
        when(carService.getAll()).thenReturn(Collections.emptyList());
        List<CarResponseDto> expected = Collections.emptyList();
        List<CarResponseDto> actual = carController.getAll();
        assertEquals(expected, actual);
    }

    @Test
    void getDetailedInfo_validId_ok() {
        car.setId(CAR_ID);
        when(carService.getById(1L)).thenReturn(car);
        when(carMapper.mapToDto(any(Car.class))).thenReturn(carResponseDto);
        CarResponseDto actual = carController.getDetailedInfo(CAR_ID);
        assertEquals(carResponseDto, actual);
    }

    @Test
    void updateCar_validId_ok() {
        car.setId(CAR_ID);
        when(carMapper.mapToEntity(any(CarRequestDto.class))).thenReturn(car);
        when(carService.update(CAR_ID, car)).thenReturn(car);
        when(carMapper.mapToDto(any(Car.class))).thenReturn(carResponseDto);
        CarResponseDto actual = carController.updateCar(CAR_ID, carRequestDto);
        assertEquals(carResponseDto, actual);
    }

    @Test
    void delete_validId_ok() {
        doNothing().when(carService).delete(CAR_ID);
        carController.delete(CAR_ID);
        verify(carService, times(1)).delete(CAR_ID);
    }
}
