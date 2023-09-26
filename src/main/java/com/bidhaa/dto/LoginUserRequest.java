package com.bidhaa.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

public record LoginUserRequest(
        @NotEmpty(message = "Email cannot be blank")
        @Email(message = "Invalid Email Address")
        String email,
        @NotEmpty
        @Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$ %^&*-]).{8,32}$",
                message = "Password must have least 8 characters," +
                        "at least one uppercase letter, one lowercase letter,one special character(@,*) and one number")
        String password
) {
}
