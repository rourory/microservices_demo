package com.microsorvices.demo.mastodontokafka;

import com.microsorvices.demo.mastodontokafka.init.StreamInitializer;
import com.microsorvices.demo.mastodontokafka.service.MastodonStreamService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@ComponentScan(basePackages = "com.microsorvices.demo")
@Slf4j
public class MastodonToKafkaServiceApplication implements CommandLineRunner {

    private final MastodonStreamService streamService;
    private final StreamInitializer streamInitializer;

    public MastodonToKafkaServiceApplication(MastodonStreamService streamService, StreamInitializer streamInitializer) {
        this.streamService = streamService;
        this.streamInitializer = streamInitializer;
    }

    public static void main(String[] args) {
        SpringApplication.run(MastodonToKafkaServiceApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("The app is running...");
        streamInitializer.init();
        streamService.startStreaming();
    }

}
