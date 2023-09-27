package com.bidhaa.service;

import com.bidhaa.dto.GenericResponse;
import com.bidhaa.dto.ProductDto;
import com.bidhaa.dto.Status;
import com.bidhaa.model.Product;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    @Value(value = "${kafka.topic}")
    private String topicName;
    private final KafkaTemplate<String, String> kafkaTemplate;

    private final ThreadPoolTaskExecutor threadPoolTaskExecutor;
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


}
