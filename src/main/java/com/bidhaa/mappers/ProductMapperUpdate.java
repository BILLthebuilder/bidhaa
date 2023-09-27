package com.bidhaa.mappers;

import com.bidhaa.dto.ProductDto;
import com.bidhaa.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductMapperUpdate {

    @Mapping(target = "id", ignore = true)
    Product toUpdate(ProductDto request, @MappingTarget Product product);
}