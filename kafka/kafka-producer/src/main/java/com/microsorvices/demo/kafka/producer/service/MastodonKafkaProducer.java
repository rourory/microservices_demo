package com.microsorvices.demo.kafka.producer.service;

import com.microservices.demo.kafka.avro.model.MastodonTootAvroModel;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class MastodonKafkaProducer implements KafkaProducer<Long, MastodonTootAvroModel> {

    private final KafkaTemplate<Long, MastodonTootAvroModel> kafkaTemplate;

    public MastodonKafkaProducer(KafkaTemplate<Long, MastodonTootAvroModel> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void send(String topicName, Long key, MastodonTootAvroModel message) {
        log.info("Sending message='{}' to topic='{}'", message, topicName);
        CompletableFuture<SendResult<Long, MastodonTootAvroModel>> kafkaResultFuture =
                kafkaTemplate.send(topicName, key, message);
        kafkaResultFuture.handle((longMastodonTootAvroModelSendResult, throwable) -> {
            if (throwable == null) {
                RecordMetadata recordMetadata = longMastodonTootAvroModelSendResult.getRecordMetadata();
                log.info("Received new metadata. Topic: {}; Partition: {}; Offset: {}; Timestamp: {}, at time: {}",
                        recordMetadata.topic(),
                        recordMetadata.partition(),
                        recordMetadata.hasOffset(),
                        recordMetadata.timestamp(),
                        System.nanoTime());
            } else {
                log.error("Error while trying to send message {} to topic {}. {}", message.toString(), topicName, throwable.getMessage());
            }
            return longMastodonTootAvroModelSendResult;
        });
    }

    @PreDestroy
    public void close(){
        if(kafkaTemplate != null){
            log.info("Closing kafka producer...");
            kafkaTemplate.destroy();
        }
    }

}
