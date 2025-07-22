package com.microsorvices.demo.common.config;

import com.microsorvices.demo.config.RetryConfigData;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.RetryPolicy;
import org.springframework.retry.backoff.BackOffPolicy;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

@Configuration
public class RetryConfig {

    private final RetryConfigData retryConfigData;

    public RetryConfig(RetryConfigData retryConfigData) {
        this.retryConfigData = retryConfigData;
    }

    @Bean
    public RetryTemplate retryTemplate(RetryPolicy retryPolicy, BackOffPolicy backOffPolicy) {
        RetryTemplate retryTemplate = new RetryTemplate();
        retryTemplate.setBackOffPolicy(backOffPolicy);
        retryTemplate.setRetryPolicy(retryPolicy);
        return retryTemplate;
    }

    @Bean
    public RetryPolicy getRetryPolicy() {
        SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy();
        retryPolicy.setMaxAttempts(retryConfigData.getMaxAttempts());
        return retryPolicy;
    }

    @Bean
    public BackOffPolicy getBackoffPolicy(){
        ExponentialBackOffPolicy backOffPolicy = new ExponentialBackOffPolicy();
        backOffPolicy.setInitialInterval(retryConfigData.getInitialIntervalMs());
        backOffPolicy.setMaxInterval(retryConfigData.getMaxIntervalMs());
        backOffPolicy.setMultiplier(retryConfigData.getBackOffMultiplier());
        return backOffPolicy;
    }
}

















