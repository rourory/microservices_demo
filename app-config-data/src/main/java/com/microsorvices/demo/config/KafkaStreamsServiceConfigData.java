package com.microsorvices.demo.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "kafka-streams-service")
@Data
public class KafkaStreamsServiceConfigData {
    private String version;
    private String customAudience;
}
