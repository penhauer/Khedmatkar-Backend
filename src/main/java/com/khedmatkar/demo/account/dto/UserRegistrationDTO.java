package com.khedmatkar.demo.account.dto;

import lombok.AllArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@AllArgsConstructor
public class UserRegistrationDTO {

    @NotBlank
    public final String firstName;

    @NotBlank
    public final String lastName;

    @NotEmpty
    @Email
    public final String email;

    @NotEmpty
    public final String password;

    @NotEmpty
    public final String type;
}
