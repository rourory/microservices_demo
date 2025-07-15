package com.microsorvices.demo.mastodontokafkaservice;

import com.microsorvices.demo.mastodontokafkaservice.config.MastodonToKafkaServiceConfigData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Arrays;

@SpringBootApplication
@Slf4j
public class MastodonToKafkaServiceApplication implements CommandLineRunner {

    private final MastodonToKafkaServiceConfigData configData;

    public MastodonToKafkaServiceApplication(MastodonToKafkaServiceConfigData configData) {
        this.configData = configData;
    }

    public static void main(String[] args) {
        SpringApplication.run(MastodonToKafkaServiceApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("The app is running...");
        log.info(Arrays.toString(configData.getTags().toArray(new String[]{})));
    }

}
