package com.bidhaa.controller;


import com.bidhaa.dto.GenericResponse;
import com.bidhaa.dto.GetEntitiesResponse;
import com.bidhaa.dto.ProductDto;
import com.bidhaa.model.Product;
import com.bidhaa.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {


    private final ProductService productService;

    @PostMapping("/upload")
    public void upload(@RequestParam("file") MultipartFile file) throws IOException {
        productService.upload(file);
    }

    @PostMapping("create")
    public ResponseEntity<GenericResponse> signup(@RequestBody @Valid ProductDto request, Errors errors) {
        return productService.create(request, errors);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<Product>> getOne(@PathVariable String id) {
        return productService.getOne(id);
    }

    @GetMapping
    public ResponseEntity<GetEntitiesResponse<Product>> getAll( @RequestParam(name = "page", defaultValue = "0") int page,
                                                                @RequestParam(name = "size", defaultValue = "10") int size,
                                                                @RequestParam(name = "sort", required = false) String sortBy,
                                                                @RequestParam(name = "order", defaultValue = "asc") String sortOrder) {
        return productService.getAll(page, size,sortBy,sortOrder);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GenericResponse> update(@RequestBody @Valid ProductDto request, @PathVariable String id, Errors errors) {
        return productService.update(id, request, errors);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<GenericResponse> delete(@PathVariable String id) {
        return productService.delete(id);
    }
}

