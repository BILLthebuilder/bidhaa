package com.bidhaa.dto;

import jakarta.validation.constraints.NotEmpty;

import java.math.BigDecimal;

public record ProductDto(
        @NotEmpty(message = "name cannot be blank")
         String name,
        @NotEmpty(message = "description cannot be blank")
        String description,

        BigDecimal price,

        Integer quantity,
        @NotEmpty(message = "category cannot be blank")
         String category,
        @NotEmpty(message = "tags cannot be blank")
         String tags

) {
}
