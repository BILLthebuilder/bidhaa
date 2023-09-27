package com.bidhaa;

import com.bidhaa.dto.ProductDto;
import com.bidhaa.model.Product;
import com.bidhaa.repository.ProductRepository;
import com.bidhaa.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.KafkaListener;

@SpringBootApplication
@Slf4j
public class BidhaaApplication {

    @Autowired
    ProductRepository productRepository;


    public static void main(String[] args) {
        SpringApplication.run(BidhaaApplication.class, args);
    }

    @KafkaListener(topics = "bidhaa", groupId = "foo")
    public void productsListener(ConsumerRecord<String, ProductDto> message) {
        ProductDto productDto = Utils.productDtoParser(String.valueOf(message.value()));
        Product product = new Product();
        product.setName(productDto.name());
        product.setQuantity(productDto.quantity());
        product.setCategory(productDto.category());
        product.setPrice(productDto.price());
        product.setDescription(productDto.description());
        product.setTags(productDto.tags());
        productRepository.save(product);
    }

}
