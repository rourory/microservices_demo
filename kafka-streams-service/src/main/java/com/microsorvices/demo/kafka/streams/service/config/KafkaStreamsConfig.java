package com.microsorvices.demo.kafka.streams.service.config;

import com.microsorvices.demo.config.KafkaConfigData;
import com.microsorvices.demo.config.KafkaStreamsConfigData;
import io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsConfig;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
@Slf4j
public class KafkaStreamsConfig {

    private final KafkaConfigData kafkaConfigData;
    private final KafkaStreamsConfigData kafkaStreamsConfigData;

    public KafkaStreamsConfig(KafkaConfigData kafkaConfigData, KafkaStreamsConfigData kafkaStreamsConfigData) {
        this.kafkaConfigData = kafkaConfigData;
        this.kafkaStreamsConfigData = kafkaStreamsConfigData;
    }

    @Bean
    @Qualifier("streamsConfiguration")
    public Properties streamsConfiguration() {
        try (var serde = Serdes.String()) {
            Properties streamsConfiguration = new Properties();
            streamsConfiguration.put(StreamsConfig.APPLICATION_ID_CONFIG, kafkaStreamsConfigData.getApplicationId());
            streamsConfiguration.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaConfigData.getBootstrapServers());
            streamsConfiguration.put(AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, kafkaConfigData.getSchemaRegistryUrl());
            streamsConfiguration.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, serde.getClass().getName());
            streamsConfiguration.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, serde.getClass().getName());
            streamsConfiguration.put(StreamsConfig.STATE_DIR_CONFIG, kafkaStreamsConfigData.getStateFileLocation());
            return streamsConfiguration;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw e;
        }
    }
}
