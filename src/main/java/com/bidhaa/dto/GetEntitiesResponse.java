package com.bidhaa.dto;

import org.springframework.data.domain.Slice;

import java.util.List;

public record GetEntitiesResponse<T>(
        Status success, List<T> entities
) {
}
