package com.example.carsharing.dto.request;

import com.example.carsharing.model.Car;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class CarRequestDto {
    private String model;
    private String brand;
    private Car.Type type;
    private int inventory;
    private BigDecimal dailyFee;
}
