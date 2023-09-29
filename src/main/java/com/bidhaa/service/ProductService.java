package com.bidhaa.service;

import com.bidhaa.dto.GenericResponse;
import com.bidhaa.dto.GetEntitiesResponse;
import com.bidhaa.dto.ProductDto;
import com.bidhaa.model.Product;
import com.bidhaa.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigDecimal;
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


    public void upload(MultipartFile file) throws IOException {
        List<ProductDto> products = new ArrayList<>();
        try (Reader reader = new InputStreamReader(file.getInputStream());
             CSVParser csvParser = new CSVParser(reader, CSVFormat.Builder.create().setHeader().setSkipHeaderRecord(true).build())) {
            for (CSVRecord csvRecord : csvParser) {
                String name = csvRecord.get("name");
                String description = csvRecord.get("description");
                String price = csvRecord.get("price");
                String quantity = csvRecord.get("quantity");
                String category = csvRecord.get("category");
                String tags = csvRecord.get("tags");
                var product = new ProductDto(
                   name,
                    description,
                    new BigDecimal(price),
                    Integer.valueOf(quantity),
                    category,
                        tags);
            products.add(product);
            }

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

    public ResponseEntity<GetEntitiesResponse<Product>> getAll(int page, int size, String sortBy, String sortOrder) {
        return genericService.getAll(productRepository,page, size,sortBy,sortOrder);
    }

    public ResponseEntity<GenericResponse> update(String id, ProductDto request, Errors errors) {
        return genericService.update(UUID.fromString(id), request, errors, Product.class, productRepository);
    }

    public ResponseEntity<GenericResponse> delete(String id) {
        return genericService.delete(id, productRepository);
    }

}
