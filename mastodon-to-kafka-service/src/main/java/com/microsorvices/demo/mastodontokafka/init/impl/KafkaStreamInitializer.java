package com.microsorvices.demo.mastodontokafka.init.impl;

import com.microsorvices.demo.config.KafkaConfigData;
import com.microsorvices.demo.kafka.admin.client.KafkaAdminClient;
import com.microsorvices.demo.mastodontokafka.init.StreamInitializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class KafkaStreamInitializer implements StreamInitializer {

    private final KafkaConfigData kafkaConfigData;
    private final KafkaAdminClient kafkaAdminClient;

    public KafkaStreamInitializer(KafkaConfigData kafkaConfigData, KafkaAdminClient kafkaAdminClient) {
        this.kafkaConfigData = kafkaConfigData;
        this.kafkaAdminClient = kafkaAdminClient;
    }

    @Override
    public void init() {
        kafkaAdminClient.createTopics();
        kafkaAdminClient.checkSchemaRegistry();
        log.info("Topics with name {} are ready for processing!", kafkaConfigData.getTopicNamesToCreate().toArray());
    }
}
