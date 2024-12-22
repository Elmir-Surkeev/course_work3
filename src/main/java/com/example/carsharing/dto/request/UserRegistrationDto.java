package com.example.carsharing.dto.request;

import com.example.carsharing.lib.ValidEmail;
import com.example.carsharing.lib.ValidPassword;
import lombok.Data;

@Data
@ValidPassword
public class UserRegistrationDto {
    @ValidEmail
    private String email;
    private String firstName;
    private String lastName;
    private String password;
    private String repeatPassword;
}
