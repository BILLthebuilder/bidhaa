package com.bidhaa.mappers;



import com.bidhaa.dto.ProductDto;
import com.bidhaa.model.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    Product toProduct(ProductDto request);
}
