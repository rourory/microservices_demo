package com.microsorvices.demo.kafka.to.elastic.service.consumer.impl;

import com.microservices.demo.kafka.avro.model.MastodonTootAvroModel;
import com.microsorvices.demo.elastic.index.client.service.ElasticIndexClient;
import com.microsorvices.demo.elastic.index.client.service.impl.MastodonElasticIndexService;
import com.microsorvices.demo.elastic.model.index.impl.MastodonIndexModel;
import com.microsorvices.demo.kafka.to.elastic.service.consumer.KafkaConsumer;
import com.microsorvices.demo.kafka.to.elastic.service.mapper.MastodonAvroToElasticModelMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class MastodonKafkaConsumer implements KafkaConsumer<Long, MastodonTootAvroModel> {

    private final ElasticIndexClient<MastodonIndexModel> mastodonElasticIndexService;
    private final MastodonAvroToElasticModelMapper mastodonAvroToElasticModelMapper;

    public MastodonKafkaConsumer(MastodonElasticIndexService mastodonElasticIndexService,
                                 MastodonAvroToElasticModelMapper mastodonAvroToElasticModelMapper) {

        this.mastodonElasticIndexService = mastodonElasticIndexService;
        this.mastodonAvroToElasticModelMapper = mastodonAvroToElasticModelMapper;

    }

    @Override
    @KafkaListener(id = "${kafka-consume-config.consumer-group-id}", topics = "${kafka-config.topic-name}")
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

        var elasticModels = mastodonAvroToElasticModelMapper.getElasticModels(messages);
        var saved = mastodonElasticIndexService.save(elasticModels);
        log.info("Documents have been saved successfully. Amount: {}", saved.size());
    }
}
