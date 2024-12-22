package com.example.carsharing.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

import com.example.carsharing.dto.request.RentalRequestDto;
import com.example.carsharing.dto.response.RentalResponseDto;
import com.example.carsharing.model.Rental;
import com.example.carsharing.service.RentalService;
import com.example.carsharing.service.mapper.RentalMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class RentalControllerTest {
    private static final Long RENTAL_ID = 1L;
    @Mock
    private RentalService rentalService;
    @Mock
    private RentalMapper rentalMapper;
    @InjectMocks
    private RentalController rentalController;
    private RentalRequestDto rentalRequestDto;
    private RentalResponseDto rentalResponseDto;
    private Rental rental;

    @BeforeEach
    void setUp() {
        rentalRequestDto = new RentalRequestDto();
        rentalResponseDto = new RentalResponseDto();
        rental = new Rental();
    }

    @Test
    void addRental_validRentalRequestDto_created() {
        when(rentalMapper.mapToEntity(any(RentalRequestDto.class))).thenReturn(rental);
        when(rentalService.add(any(Rental.class))).thenReturn(rental);
        when(rentalMapper.mapToDto(any(Rental.class))).thenReturn(rentalResponseDto);
        ResponseEntity<RentalResponseDto> response = rentalController.addRental(rentalRequestDto);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(rentalResponseDto, response.getBody());
    }

    @Test
    void getRentalById_validId_ok() {
        when(rentalService.getById(RENTAL_ID)).thenReturn(rental);
        when(rentalMapper.mapToDto(any(Rental.class))).thenReturn(rentalResponseDto);
        ResponseEntity<RentalResponseDto> response = rentalController.getRentalById(RENTAL_ID);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(rentalResponseDto, response.getBody());
    }

    @Test
    void returnRental_validId_ok() {
        when(rentalService.terminate(RENTAL_ID)).thenReturn(rental);
        when(rentalMapper.mapToDto(any(Rental.class))).thenReturn(rentalResponseDto);
        ResponseEntity<RentalResponseDto> response = rentalController.returnRental(RENTAL_ID);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(rentalResponseDto, response.getBody());
    }
}
