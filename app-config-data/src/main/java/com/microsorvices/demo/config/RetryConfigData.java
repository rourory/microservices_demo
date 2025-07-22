package com.microsorvices.demo.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "retry-config")
public class RetryConfigData {
    private Long initialIntervalMs;
    private Long maxIntervalMs;
    private Double backOffMultiplier;
    private Integer maxAttempts;
    private Long sleepTimeMs;
}
