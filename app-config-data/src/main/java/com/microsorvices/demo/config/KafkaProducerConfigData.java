package com.microsorvices.demo.config;

import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "kafka-producer-config")
@Slf4j
public class KafkaProducerConfigData {
    private String keySerializerClass;
    private String valueSerializerClass;
    private String compressionType;
    private String acks;
    private Integer batchSize;
    private Integer batchSizeBoostFactor;
    private Integer lingerMs;
    private Integer requestTimeoutMs;
    private Integer retryCount;

    @PostConstruct
    public void onConstruct() {
        log.info("""
                        Kafka config data has initialized with next properties: keySerializerClass: {}
                        valueSerializerClass: {}
                        compressionType: {}
                        acks : {}
                        batchSize : {}
                        batchSizeBoostFactor : {}
                        lingerMs : {}
                        requestTimeoutMs : {}
                        retryCount : {}""", keySerializerClass,
                valueSerializerClass,
                compressionType,
                acks,
                batchSize,
                batchSizeBoostFactor,
                lingerMs,
                requestTimeoutMs,
                retryCount);
    }
}
