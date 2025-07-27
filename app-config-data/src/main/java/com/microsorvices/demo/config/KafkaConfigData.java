package com.microsorvices.demo.config;

import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


import java.util.List;

@Data
@Configuration
@ConfigurationProperties(prefix = "kafka-config")
@Slf4j
public class KafkaConfigData {
    private String bootstrapServers;
    private String schemaRegistryUrlKey;
    private String schemaRegistryUrl;
    private String topicName;
    private List<String> topicNamesToCreate;
    private Integer numberOfPartitions;
    private Short replicationFactor;

    @PostConstruct
    public void onConstruct() {
        log.info("""
                        Kafka config data has initialized with next properties: bootstrapServers: {}
                        schemaRegistryUrlKey: {}
                        schemaRegistryUrl: {}
                        topicName : {}
                        topicNamesToCreate : {}
                        numberOfPartitions : {}
                        replicationFactor : {}""", bootstrapServers,
                schemaRegistryUrlKey,
                schemaRegistryUrl,
                topicName,
                topicNamesToCreate,
                numberOfPartitions,
                replicationFactor);
    }

    @Override
    public String toString() {
        return "KafkaConfigData{" +
                "bootstrapServers='" + bootstrapServers + '\'' +
                ", schemaRegistryUrlKey='" + schemaRegistryUrlKey + '\'' +
                ", schemaRegistryUrl='" + schemaRegistryUrl + '\'' +
                ", topicName='" + topicName + '\'' +
                ", topicNamesToCreate=" + topicNamesToCreate +
                ", numberOfPartitions=" + numberOfPartitions +
                ", replicationFactor=" + replicationFactor +
                '}';
    }
}
