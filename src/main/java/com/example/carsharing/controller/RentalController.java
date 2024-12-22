package com.example.carsharing.controller;

import com.example.carsharing.dto.request.RentalRequestDto;
import com.example.carsharing.dto.response.RentalResponseDto;
import com.example.carsharing.model.Rental;
import com.example.carsharing.model.User;
import com.example.carsharing.service.RentalService;
import com.example.carsharing.service.UserService;
import com.example.carsharing.service.mapper.RentalMapper;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/rentals")
public class RentalController {
    private final RentalService rentalService;
    private final RentalMapper rentalMapper;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<RentalResponseDto> addRental(@RequestBody
                                                       RentalRequestDto rentalRequestDto) {
        Rental rental = rentalMapper.mapToEntity(rentalRequestDto);
        Rental addedRental = rentalService.add(rental);
        RentalResponseDto rentalResponseDto = rentalMapper.mapToDto(addedRental);
        return new ResponseEntity<>(rentalResponseDto, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<RentalResponseDto>> getRentals(Authentication authentication,
                                                              @RequestParam boolean isActive) {
        User user = userService.getByEmail(authentication.getName()).get();
        List<Rental> rentals = rentalService.getByUserAndActiveness(user, isActive);
        List<RentalResponseDto> rentalResponseDtos = rentals.stream()
                .map(rentalMapper::mapToDto)
                .collect(Collectors.toList());
        return new ResponseEntity<>(rentalResponseDtos, HttpStatus.OK);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<List<RentalResponseDto>> getRentalByUserId(@PathVariable Long id,
                                                              @RequestParam boolean isActive) {
        User user = new User();
        user.setId(id);
        List<Rental> rentals = rentalService.getByUserAndActiveness(user, isActive);
        List<RentalResponseDto> rentalResponseDtos = rentals.stream()
                .map(rentalMapper::mapToDto)
                .collect(Collectors.toList());
        return new ResponseEntity<>(rentalResponseDtos, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RentalResponseDto> getRentalById(@PathVariable Long id) {
        Rental rental = rentalService.getById(id);
        RentalResponseDto rentalResponseDto = rentalMapper.mapToDto(rental);
        return new ResponseEntity<>(rentalResponseDto, HttpStatus.OK);
    }

    @PostMapping("/{id}/return")
    public ResponseEntity<RentalResponseDto> returnRental(@PathVariable Long id) {
        Rental rental = rentalService.terminate(id);
        RentalResponseDto rentalResponseDto = rentalMapper.mapToDto(rental);
        return new ResponseEntity<>(rentalResponseDto, HttpStatus.OK);
    }
}
