package com.example.carsharing.lib;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class EmailValidatorTest {
    private static ConstraintValidatorContext context;
    private static EmailValidator emailValidator;
    private static String email;

    @BeforeAll
    static void beforeAll() {
        emailValidator = new EmailValidator();
    }

    @Test
    void isValid_properEmail_ok() {
        email = "test@test.com";
        assertTrue(emailValidator.isValid(email, context),
                String.format("Passed email: '%s' is valid. Method should return 'true", email));
    }

    @Test
    void isValid_withoutTopDomain_notOk() {
        email = "test@test.";
        assertFalse(emailValidator.isValid(email, context),
                String.format("Passed email: '%s' is not valid. Method should return 'false",
                        email));
    }

    @Test
    void isValid_withoutAtSign_notOk() {
        email = "test_test.com";
        assertFalse(emailValidator.isValid(email, context),
                String.format("Passed email: '%s' is not valid. Method should return 'false",
                        email));
    }

    @Test
    void isValid_nullEmail_notOk() {
        email = null;
        assertFalse(emailValidator.isValid(email, context),
                "Passed email is null. Method should return 'false");
    }

    @Test
    void isValid_emptyEmail_notOk() {
        email = "";
        assertFalse(emailValidator.isValid(email, context),
                "Passed email is empty. Method should return 'false");
    }
}
