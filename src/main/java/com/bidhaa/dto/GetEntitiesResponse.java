package com.bidhaa.dto;

import org.springframework.data.domain.Page;

public record GetEntitiesResponse<T>(
        Status success, Page<T> entities
) {
}
