package com.bidhaa.service;

import com.bidhaa.dto.*;
import com.bidhaa.mappers.ProductMapper;
import com.bidhaa.mappers.ProductMapperUpdate;
import com.bidhaa.model.Product;
import com.bidhaa.model.User;
import com.bidhaa.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    @Value(value = "${kafka.topic}")
    private String topicName;
    private final KafkaTemplate<String, String> kafkaTemplate;

    private final ThreadPoolTaskExecutor threadPoolTaskExecutor;

    private final ProductRepository productRepository;

    private final GenericService genericService;

    private final ProductMapper productMapper;

    private final ProductMapperUpdate productMapperUpdate;

    public void upload(MultipartFile file) throws IOException {
        List<ProductDto> products = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] fields = line.split(",");

            var product = new ProductDto(fields[0], fields[1], fields[2], fields[3], fields[4], fields[5]);
            products.add(product);
        }
        for (ProductDto product : products) {
            threadPoolTaskExecutor.submit(() -> kafkaTemplate.send(topicName, String.valueOf(product)));
        }
    }

    public ResponseEntity<GenericResponse> create(ProductDto request, Errors errors) {
        return genericService.create(request, errors, Product.class, productRepository);
    }

    public ResponseEntity<Optional<Product>> getOne(String id) {
        return genericService.getOne(id, productRepository);
    }

    public ResponseEntity<GetEntitiesResponse<Product>> getAll(int page, int size) {
        return genericService.getAll(page, size, productRepository);
    }

    public ResponseEntity<GenericResponse> update(String id, ProductDto request, Errors errors) {
        return genericService.update(UUID.fromString(id), request, errors, Product.class, productRepository);
    }

    public ResponseEntity<GenericResponse> delete(String id) {
        return genericService.delete(id, productRepository);
    }

}
