package com.example.carsharing.service.mapper;

import com.example.carsharing.config.MapperConfig;
import com.example.carsharing.dto.response.PaymentResponseDto;
import com.example.carsharing.model.Payment;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface PaymentMapper {
    PaymentResponseDto mapToDto(Payment payment);
}
