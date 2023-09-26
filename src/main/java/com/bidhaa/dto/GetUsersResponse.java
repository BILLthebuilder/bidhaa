package com.bidhaa.dto;

import com.bidhaa.model.User;

import java.util.List;

public record GetUsersResponse(
        Status status,
        List<User>users
) {
}
