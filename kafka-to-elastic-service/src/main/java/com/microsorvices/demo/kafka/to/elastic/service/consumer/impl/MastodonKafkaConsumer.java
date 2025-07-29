package com.microsorvices.demo.kafka.to.elastic.service.consumer.impl;

import com.microservices.demo.kafka.avro.model.MastodonTootAvroModel;
import com.microsorvices.demo.config.KafkaConfigData;
import com.microsorvices.demo.kafka.admin.client.KafkaAdminClient;
import com.microsorvices.demo.kafka.to.elastic.service.consumer.KafkaConsumer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class MastodonKafkaConsumer implements KafkaConsumer<Long, MastodonTootAvroModel> {

    //    private final KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;
    private final KafkaAdminClient kafkaAdminClient;
    private final KafkaConfigData kafkaConfigData;

    public MastodonKafkaConsumer(KafkaAdminClient kafkaAdminClient,
                                 KafkaConfigData kafkaConfigData) {
        this.kafkaAdminClient = kafkaAdminClient;
        this.kafkaConfigData = kafkaConfigData;
    }

    @Override
    @KafkaListener(id = "mastodonTopicListener", topics = "${kafka-config.topic-name}")
    public void receive(@Payload List<MastodonTootAvroModel> messages,
                        @Header(KafkaHeaders.RECEIVED_KEY) List<Integer> keys,
                        @Header(KafkaHeaders.RECEIVED_PARTITION) List<Integer> partitions,
                        @Header(KafkaHeaders.OFFSET) List<Long> offsets) {
        log.info("""
                        {} number of messages received with keys {}, partitions {}, offsets {}. 
                        Sending them to elastic: Thread id {} 
                        """,
                messages.size(),
                keys.toString(),
                partitions.toString(),
                offsets.toString(),
                Thread.currentThread().getId());
    }
}
