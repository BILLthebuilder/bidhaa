package com.bidhaa.mappers;

import com.bidhaa.dto.CreateUserRequest;
import com.bidhaa.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
User toUser(CreateUserRequest request);
}

