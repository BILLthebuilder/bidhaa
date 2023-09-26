package com.bidhaa.dto;

import jakarta.validation.constraints.Email;

public record UpdateUserRequest(
        String firstName,
        String lastName,
        @Email(message = "Invalid Email Address")
        String email,
        String phoneNumber
) {
}
