package com.microsorvices.demo.kafka.streams.service;

import com.microsorvices.demo.kafka.streams.service.runner.StreamsRunner;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import com.microsorvices.demo.kafka.streams.service.init.impl.KafkaStreamsInitializer;

@SpringBootApplication
@ComponentScan(basePackages = {"com.microsorvices.demo"})
@Slf4j
public class KafkaStreamsServiceApplication implements CommandLineRunner {

    private final StreamsRunner<String, Long> streamRunner;
    private final KafkaStreamsInitializer streamInitializer;

    public KafkaStreamsServiceApplication(StreamsRunner<String, Long> streamsRunner,
                                          KafkaStreamsInitializer streamsInitializer) {
        this.streamRunner = streamsRunner;
        this.streamInitializer = streamsInitializer;
    }

    public static void main(String[] args) {
        SpringApplication.run(KafkaStreamsServiceApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("Starting Kafka Streams Service");
        streamInitializer.init();
        streamRunner.start();
    }
}
