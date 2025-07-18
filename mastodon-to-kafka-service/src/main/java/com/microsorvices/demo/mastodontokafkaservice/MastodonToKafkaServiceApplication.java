package com.microsorvices.demo.mastodontokafkaservice;

import com.microsorvices.demo.mastodontokafkaservice.service.MastodonStreamService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class MastodonToKafkaServiceApplication implements CommandLineRunner {

    private final MastodonStreamService streamService;

    public MastodonToKafkaServiceApplication(MastodonStreamService streamService) {
        this.streamService = streamService;
    }

    public static void main(String[] args) {
        SpringApplication.run(MastodonToKafkaServiceApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("The app is running...");
        streamService.startStreaming();
    }

}
