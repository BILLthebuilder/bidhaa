package com.bidhaa.dto;

import com.bidhaa.model.Product;

public record GetProductsResponse(
        Status status,
        org.springframework.data.domain.Page<Product> products
) {
}