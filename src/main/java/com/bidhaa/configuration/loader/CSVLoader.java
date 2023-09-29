package com.bidhaa.configuration.loader;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Random;

@Component
@Slf4j
public class CSVLoader implements ApplicationListener<ContextRefreshedEvent> {

    @Override

    public void onApplicationEvent(ContextRefreshedEvent event) {
        String[] headers = {"name", "description", "price", "quantity", "category", "tags"};

        try (CSVPrinter csvPrinter = new CSVPrinter(new FileWriter("products.csv"), CSVFormat.DEFAULT.withHeader(headers))) {
            Random random = new Random();
            int numberOfRecords = 1000; // Change as needed

            for (int i = 0; i < numberOfRecords; i++) {
                String name = "Product" + (i + 1);
                String description = "Description" + (i + 1);
                BigDecimal price = BigDecimal.valueOf(10 + random.nextDouble() * 90); // Random price between 10 and 100
                Integer quantity = random.nextInt(100); // Random quantity between 0 and 99
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
