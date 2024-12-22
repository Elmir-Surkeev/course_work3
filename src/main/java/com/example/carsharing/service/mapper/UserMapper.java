package com.example.carsharing.service.mapper;

import com.example.carsharing.config.MapperConfig;
import com.example.carsharing.dto.request.UserRegistrationDto;
import com.example.carsharing.dto.response.UserResponseDto;
import com.example.carsharing.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface UserMapper {
    UserResponseDto mapToDto(User user);

    @Mapping(target = "id", ignore = true)
    User mapToEntity(UserRegistrationDto userDto);
}
