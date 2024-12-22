package com.example.carsharing.lib;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.carsharing.dto.request.UserRegistrationDto;
import javax.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class PasswordValidatorTest {
    private static ConstraintValidatorContext context;
    private static PasswordValidator passwordValidator;
    private static UserRegistrationDto userRegistrationDto;

    @BeforeAll
    static void beforeAll() {
        passwordValidator = new PasswordValidator();
        userRegistrationDto = new UserRegistrationDto();
    }

    @Test
    void isValid_properUserDto_ok() {
        userRegistrationDto.setPassword("12345678");
        userRegistrationDto.setRepeatPassword("12345678");
        assertTrue(passwordValidator.isValid(userRegistrationDto, context),
                String.format("Method should return 'true' when password = '%s' and repeated "
                                + "password = '%s'", userRegistrationDto.getPassword(),
                        userRegistrationDto.getRepeatPassword()));
    }

    @Test
    void isValid_nonMatchingPasswords_notOk() {
        userRegistrationDto.setPassword("12345678");
        userRegistrationDto.setRepeatPassword("87654321");
        assertFalse(passwordValidator.isValid(userRegistrationDto, context),
                String.format("Method should return 'false' when password = '%s' and repeated "
                                + "password = '%s'", userRegistrationDto.getPassword(),
                        userRegistrationDto.getRepeatPassword()));
    }

    @Test
    void isValid_toShortPassword_notOk() {
        userRegistrationDto.setPassword("short");
        assertFalse(passwordValidator.isValid(userRegistrationDto, context),
                String.format("Method should return 'false' when password is to short. Password = "
                        + "'%s'", userRegistrationDto.getPassword()));
    }

    @Test
    void isValid_emptyPassword_notOk() {
        userRegistrationDto.setPassword("");
        assertFalse(passwordValidator.isValid(userRegistrationDto, context),
                String.format("Method should return 'false' when password is empty. Password = "
                        + "'%s'", userRegistrationDto.getPassword()));
    }

    @Test
    void isValid_nullPassword_notOk() {
        userRegistrationDto.setPassword(null);
        assertFalse(passwordValidator.isValid(userRegistrationDto, context),
                "Method should return 'false' when password is null");
    }
}
