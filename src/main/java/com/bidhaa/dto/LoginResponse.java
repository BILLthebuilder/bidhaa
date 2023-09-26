package com.bidhaa.dto;


public record LoginResponse(
        String message,
        String token,
        Status status

) {
}

