package com.bidhaa.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.web.exchanges.HttpExchange;
import org.springframework.boot.actuate.web.exchanges.InMemoryHttpExchangeRepository;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Repository;
import java.util.HashMap;

@Repository
@Configuration
@RequiredArgsConstructor
@Slf4j
public class KafkaHttpExchangeRepository extends InMemoryHttpExchangeRepository {
    private final KafkaTemplate kafkaTemplate;

    @Override
    public void add(HttpExchange exchange) {
        super.add(exchange);
        String timestamp = exchange.getTimestamp().toString();
        String headers = exchange.getRequest().getHeaders().toString();
        String uri = exchange.getRequest().getUri().toString();
        String method = exchange.getRequest().getMethod();
        String responseStatus = String.valueOf(exchange.getResponse().getStatus());
        String timeTaken = exchange.getTimeTaken().toString();

        HashMap<String,String> auditTrail = new HashMap<>();
        auditTrail.put("timestamp",timestamp);
        auditTrail.put("headers",headers);
        auditTrail.put("uri",uri);
        auditTrail.put("method",method);
        auditTrail.put("responseStatus",responseStatus);
        auditTrail.put("timeTaken",timeTaken);

        kafkaTemplate.send("logging", auditTrail.toString());
    }
}
