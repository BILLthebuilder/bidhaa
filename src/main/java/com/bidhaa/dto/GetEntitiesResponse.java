package com.bidhaa.dto;

import java.util.List;

public record GetEntitiesResponse<T>(
        Status success, List<T> entities
) {
}
