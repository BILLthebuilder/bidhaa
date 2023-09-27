package com.bidhaa.configuration.loader;

import com.bidhaa.configuration.kafka.KafkaTopicConfig;
import com.bidhaa.dto.ProductDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.ListTopicsOptions;
import org.apache.kafka.clients.admin.ListTopicsResult;
import org.apache.kafka.common.KafkaFuture;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaLoader implements ApplicationListener<ContextRefreshedEvent> {

    private final KafkaTopicConfig kafkaTopicConfig;


    @Override

    public void onApplicationEvent(ContextRefreshedEvent event) {
        Map<String, Object> config = kafkaTopicConfig.kafkaAdmin().getConfigurationProperties();
        try (AdminClient adminClient = AdminClient.create(config)) {
            ListTopicsOptions options = new ListTopicsOptions();
            options.listInternal(true); // Include internal topics if needed
            ListTopicsResult topicsResult = adminClient.listTopics(options);
            KafkaFuture<Set<String>> names = topicsResult.names();
           log.info("Topic created>>>>",adminClient.listTopics());
        }catch (Exception e){
          log.error("Error During topic creation",e);
        }
        String[] headers = {"name", "description", "price", "quantity", "category", "tags"};

        try (CSVPrinter csvPrinter = new CSVPrinter(new FileWriter("products.csv"), CSVFormat.DEFAULT.withHeader(headers))) {
            Random random = new Random();
            int numberOfRecords = 1000000; // Change as needed

            for (int i = 0; i < numberOfRecords; i++) {
                String name = "Product" + (i + 1);
                String description = "Description" + (i + 1);
                double price = 10 + random.nextDouble() * 90; // Random price between 10 and 100
                int quantity = random.nextInt(100); // Random quantity between 0 and 99
                String category = "Category" + (i % 5); // Five different categories
                String tags = "Tag" + (i % 3); // Three different tags

                csvPrinter.printRecord(name, description, price, quantity, category, tags);
            }

            log.info("CSV file generated successfully.");
        } catch (IOException e) {
            log.error(String.valueOf(e));
        }

    }

}
