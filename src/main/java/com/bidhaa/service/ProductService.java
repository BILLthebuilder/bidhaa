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
                //kafkaTemplate.send(topicName, String.valueOf(product));
 //               CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(topicName, String.valueOf(product));
//                future.whenComplete((result, ex) -> {
//                    if (ex == null) {
//                        log.info("Sent message=[" + product +
//                                "] with offset=[" + result.getRecordMetadata().offset() + "]");
//                    } else {
//                        log.error("Unable to send message=[" +
//                                product + "] due to : " + ex.getMessage());
//                    }
//                });
            }
    }


    @Transactional
    public ResponseEntity<GenericResponse> create(ProductDto request, Errors errors) {
        GenericResponse response;

        if (errors.hasFieldErrors()) {
            FieldError fieldError = errors.getFieldError();
            response = new GenericResponse(fieldError.getDefaultMessage(), Status.FAILED);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(response);
        }
        try {
                var product = productMapper.toProduct(request);
                productRepository.save(product);
                response = new GenericResponse("Product created successfully", Status.SUCCESS);
                return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception ex) {
            StringWriter sw = new StringWriter();
            ex.printStackTrace(new PrintWriter(sw));
            log.error("Unable to create product",sw);
            response = new GenericResponse(ex.getMessage(), Status.FAILED);
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    public ResponseEntity<Optional<Product>> getOne(String id){
        if(productExists(id)){
            return ResponseEntity.ok(productRepository.findById(UUID.fromString(id)));
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }
    public ResponseEntity<GetProductsResponse> getAll(int page , int size) {
        GetProductsResponse response;
        Page<Product> products = null;
        Pageable pageable = PageRequest.of(page, size);
        try {
            products = productRepository.findAll(pageable);
            if (!products.isEmpty()) {
                response = new GetProductsResponse(Status.SUCCESS, products);
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                response = new GetProductsResponse(Status.SUCCESS, products);
                return new ResponseEntity<>(response, HttpStatus.OK);
            }

        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            log.error("Error has occured", sw);
            response = new GetProductsResponse(Status.SUCCESS, products);
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @Transactional
    public ResponseEntity<GenericResponse> update(String id, ProductDto request, Errors errors) {
        GenericResponse response;
        try {
            if (errors.hasFieldErrors()) {
                FieldError fieldError = errors.getFieldError();
                response = new GenericResponse(fieldError.getDefaultMessage(), Status.FAILED);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(response);
            }
            var product = productRepository.findById(UUID.fromString(id));
            productMapperUpdate.toUpdate(request, product.get());
            productRepository.save(product.get());
            response = new GenericResponse("Product updated successfully", Status.SUCCESS);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception ex) {
            StringWriter sw = new StringWriter();
            ex.printStackTrace(new PrintWriter(sw));
            log.error("Unable to update product=%s", sw);
            response = new GenericResponse("update failed", Status.FAILED);
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }
    @Transactional
    public ResponseEntity<GenericResponse> delete(String id) {
        GenericResponse response;
        try {
            if (!productExists(id)) {
                response = new GenericResponse("Product not Found", Status.FAILED);
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
            productRepository.deleteById(UUID.fromString(id));
            response = new GenericResponse("Product deleted successfully", Status.SUCCESS);
            return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
        } catch (Exception ex) {
            StringWriter sw = new StringWriter();
            ex.printStackTrace(new PrintWriter(sw));
            log.error("Unable to delete Product=%s", sw);
            response = new GenericResponse("Product deleting failed", Status.FAILED);
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }

    }

    public boolean productExists(String id) {
        boolean isPresent = false;
        if (productRepository.findById(UUID.fromString(id)) != null) {
            isPresent = true;
        }
        return isPresent;
    }
}
