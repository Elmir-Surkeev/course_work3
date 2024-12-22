package com.example.carsharing.service.mapper;

import com.example.carsharing.config.MapperConfig;
import com.example.carsharing.dto.request.RentalRequestDto;
import com.example.carsharing.dto.response.RentalResponseDto;
import com.example.carsharing.model.Rental;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface RentalMapper {
    @Mapping(target = "carId", source = "rental.car.id")
    @Mapping(target = "userId", source = "rental.user.id")
    RentalResponseDto mapToDto(Rental rental);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "rentalDate", ignore = true)
    @Mapping(target = "actualReturnDate", ignore = true)
    @Mapping(source = "carId", target = "car.id")
    @Mapping(source = "userId", target = "user.id")
    Rental mapToEntity(RentalRequestDto rentalRequestDto);
}
