package com.example.carsharing.dto.request;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class RentalRequestDto {
    private LocalDateTime returnDate;
    private Long carId;
    private Long userId;
}
