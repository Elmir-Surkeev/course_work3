package com.example.carsharing.lib;

import com.example.carsharing.dto.request.UserRegistrationDto;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<ValidPassword, UserRegistrationDto> {
    private static final int MIN_PASSWORD_SIZE = 8;

    @Override
    public boolean isValid(UserRegistrationDto userRequestDto, ConstraintValidatorContext context) {
        return userRequestDto.getPassword() != null
                && userRequestDto.getPassword().length() >= MIN_PASSWORD_SIZE
                && userRequestDto.getPassword().equals(userRequestDto.getRepeatPassword());
    }
}
