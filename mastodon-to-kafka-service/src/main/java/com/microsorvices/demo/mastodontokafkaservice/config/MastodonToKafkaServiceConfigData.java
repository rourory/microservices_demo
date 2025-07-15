package com.microsorvices.demo.mastodontokafkaservice.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Data
@Configuration
@ConfigurationProperties(prefix = "mastodon")
public class MastodonToKafkaServiceConfigData {
    private String instance;
    private String accessToken;
    private String secretKey;
    private String appId;
    private List<String> tags;
}