package com.microsorvices.demo.kafka.streams.service.runner.impl;

import com.microservices.demo.kafka.avro.model.MastodonAnalyticsAvroModel;
import com.microservices.demo.kafka.avro.model.MastodonTootAvroModel;
import com.microsorvices.demo.config.KafkaConfigData;
import com.microsorvices.demo.config.KafkaStreamsConfigData;
import com.microsorvices.demo.kafka.streams.service.runner.StreamsRunner;
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Pattern;

@Component
@Slf4j
public class KafkaStreamsRunner implements StreamsRunner<String, Long> {

    private static final String REGEX = "\\W+";

    private final KafkaStreamsConfigData kafkaStreamsConfigData;
    private final KafkaConfigData kafkaConfigData;
    private final Properties streamsConfiguration;
    private KafkaStreams kafkaStreams;

    //6:20
    //6:20
    //6:20
    //6:20
    //6:20

    public KafkaStreamsRunner(KafkaStreamsConfigData kafkaStreamsConfigData,
                              KafkaConfigData kafkaConfigData,
                              @Qualifier("streamsConfiguration") Properties streamsConfiguration) {
        this.kafkaStreamsConfigData = kafkaStreamsConfigData;
        this.kafkaConfigData = kafkaConfigData;
        this.streamsConfiguration = streamsConfiguration;
    }

    @Override
    public void start() {
        final Map<String, String> serdeConfig = Collections.singletonMap(
                kafkaConfigData.getSchemaRegistryUrlKey(),
                kafkaConfigData.getSchemaRegistryUrl()
        );
        final StreamsBuilder streamsBuilder = new StreamsBuilder();
        var mastodonAvroModelKStream = getMastodonAvroModelKStream(serdeConfig, streamsBuilder);
        createTopology(mastodonAvroModelKStream, serdeConfig);
        startStreaming(streamsBuilder);
    }

    @Override
    public Long getValueByKey(String key) {
        return StreamsRunner.super.getValueByKey(key);
    }

    @PreDestroy
    public void close(){
        if(kafkaStreams != null) {
            kafkaStreams.close();
            log.info("Kafka streams closed!");
        }
    }

    private void startStreaming(StreamsBuilder streamsBuilder) {
        final Topology topology = streamsBuilder.build();
        log.info("Defined topology : {}", topology.describe());
        kafkaStreams = new KafkaStreams(topology, streamsConfiguration);
        kafkaStreams.start();
        log.info("Started Kafka Streams");
    }

    @SuppressWarnings("unchecked")
    private void createTopology(KStream<Long, MastodonTootAvroModel> mastodonAvroModelKStream,
                                Map<String, String> serdeConfig) {
        var pattern = Pattern.compile(REGEX, Pattern.UNICODE_CHARACTER_CLASS);
        Serde<MastodonAnalyticsAvroModel> serdeAnalyticsModel = getSerdeAnalyticsModel(serdeConfig);
        Produced<?, ?> produced = Produced.with(Serdes.String(), serdeAnalyticsModel);
        mastodonAvroModelKStream.flatMapValues(value ->
                        Arrays.asList(pattern.split(value.getContent().toLowerCase())))
                .groupBy((key, word) -> word)
                .count(Materialized.as(kafkaStreamsConfigData.getWordCountStoreName()))
                .toStream()
                .map(mapToAnalyticModel())
                .to(kafkaStreamsConfigData.getOutputTopicName(),
                        (Produced<Object, Object>) produced);
    }

    private KeyValueMapper<? super String, ? super Long, ? extends KeyValue<?, ?>> mapToAnalyticModel() {
        return (word, count) -> {
            log.info("Sending to topic {}, word {} - count {}",
                    kafkaStreamsConfigData.getOutputTopicName(), word, count);
            return new KeyValue<>(word, MastodonAnalyticsAvroModel
                    .newBuilder()
                    .setWord(word)
                    .setWordCount(count)
                    .setCreatedAt(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC))
                    .build());
        };
    }

    private Serde<MastodonAnalyticsAvroModel> getSerdeAnalyticsModel(Map<String, String> serdeConfig) {
        Serde<MastodonAnalyticsAvroModel> serdeAnalyticsModel = new SpecificAvroSerde<>();
        serdeAnalyticsModel.configure(serdeConfig, false);
        return serdeAnalyticsModel;
    }

    private KStream<Long, MastodonTootAvroModel> getMastodonAvroModelKStream(Map<String, String> serdeConfig, StreamsBuilder streamsBuilder) {
        final Serde<MastodonTootAvroModel> serdeMastodonAvroModel = new SpecificAvroSerde<>();
        serdeMastodonAvroModel.configure(serdeConfig, false);
        return streamsBuilder.stream(kafkaStreamsConfigData.getInputTopicMame(),
                Consumed.with(Serdes.Long(), serdeMastodonAvroModel));

    }

}
