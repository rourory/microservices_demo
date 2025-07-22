package com.microsorvices.demo.mastodontokafka.handler;

import com.microservices.demo.kafka.avro.model.MastodonTootAvroModel;
import com.microsorvices.demo.config.KafkaConfigData;
import com.microsorvices.demo.kafka.producer.service.KafkaProducer;
import com.microsorvices.demo.mastodontokafka.transformer.MastodonAvroTransformer;
import com.sys1yagi.mastodon4j.api.Handler;
import com.sys1yagi.mastodon4j.api.entity.Status;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;


@Component
@Slf4j
public class StandardMastodonHandler implements Handler {

    private final MastodonAvroTransformer mastodonAvroTransformer;
    private final KafkaProducer<Long, MastodonTootAvroModel> kafkaProducer;
    private final KafkaConfigData kafkaConfigData;

    public StandardMastodonHandler(MastodonAvroTransformer mastodonAvroTransformer,
                                   KafkaProducer<Long, MastodonTootAvroModel> kafkaProducer,
                                   KafkaConfigData kafkaConfigData) {
        this.mastodonAvroTransformer = mastodonAvroTransformer;
        this.kafkaProducer = kafkaProducer;
        this.kafkaConfigData = kafkaConfigData;
    }

    @Override
    public void onStatus(Status status) {
        var message = mastodonAvroTransformer.transformFrom(status);
        kafkaProducer.send(kafkaConfigData.getTopicName(), message.getId(), message);
    }

    @Override
    public void onDelete(long l) {
        log.info("Deleted toot ID: {}", l);
    }

    @Override
    public void onNotification(@NotNull com.sys1yagi.mastodon4j.api.entity.Notification notification) {
        // Ignore notifications
    }

}
