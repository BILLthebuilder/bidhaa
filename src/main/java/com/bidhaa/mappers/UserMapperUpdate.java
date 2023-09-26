package com.bidhaa.mappers;

import com.bidhaa.dto.UpdateUserRequest;
import com.bidhaa.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapperUpdate {

    @Mapping(target = "id", ignore = true)
    User toUpdate(UpdateUserRequest request,@MappingTarget User user);
}
