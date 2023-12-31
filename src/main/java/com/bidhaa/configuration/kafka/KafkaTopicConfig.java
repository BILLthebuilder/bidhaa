package com.bidhaa.configuration.kafka;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaTopicConfig {
    @Value(value = "${spring.kafka.bootstrap-servers}")
    private String bootstrapAddress;

    @Value(value = "${kafka.topic}")
    private String topicName;

    @Value(value = "${kafka.logging}")
    private String loggingTopic;

    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        return new org.springframework.kafka.core.KafkaAdmin(configs);
    }
    @Bean
    public NewTopic productsTopic() {
        return new NewTopic(topicName, 4, (short) 1);
    }
    @Bean
    public NewTopic loggingTopic() {
        return new NewTopic(loggingTopic, 4, (short) 1);
    }
}