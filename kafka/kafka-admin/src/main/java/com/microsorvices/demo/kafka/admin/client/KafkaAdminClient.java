package com.microsorvices.demo.kafka.admin.client;

import com.microsorvices.demo.config.KafkaConfigData;
import com.microsorvices.demo.config.RetryConfigData;
import com.microsorvices.demo.kafka.admin.exception.KafkaClientException;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.CreateTopicsResult;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.admin.TopicListing;
import org.springframework.http.HttpStatus;
import org.springframework.retry.RetryContext;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Collection;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Component
@Slf4j
public class KafkaAdminClient {
    private final KafkaConfigData kafkaConfigData;
    private final RetryConfigData retryConfigData;
    private final AdminClient adminClient;
    private final RetryTemplate retryTemplate;
    private final WebClient webClient;

    public KafkaAdminClient(KafkaConfigData kafkaConfigData,
                            RetryConfigData retryConfigData,
                            AdminClient adminClient,
                            RetryTemplate retryTemplate,
                            WebClient webClient) {
        this.kafkaConfigData = kafkaConfigData;
        this.retryConfigData = retryConfigData;
        this.adminClient = adminClient;
        this.retryTemplate = retryTemplate;
        this.webClient = webClient;
    }

    public void createTopics() {
        log.info("Start creating topics...");
        CreateTopicsResult createTopicsResult;
        try {
            createTopicsResult = retryTemplate.execute(this::doCreateTopics);
            log.info("Create topic result {}", createTopicsResult.values().values());
        } catch (Throwable t) {
            throw new KafkaClientException("Reached max numbers of retry creating kafka topic(s)", t);
        }
        log.info("Start checking if topics are created...");
        checkTopicsCreated();
        log.info("Topics are created!");
    }

    private CreateTopicsResult doCreateTopics(RetryContext retryContext) {
        var topicNames = kafkaConfigData.getTopicNamesToCreate();
        log.info("Creating {} topic(s), attempt: {}", topicNames.size(), retryContext.getRetryCount());
        var kafkaTopics = topicNames.stream().map(topic ->
                        new NewTopic(topic.trim(),
                                kafkaConfigData.getNumberOfPartitions(),
                                kafkaConfigData.getReplicationFactor()))
                .collect(Collectors.toList());
        return adminClient.createTopics(kafkaTopics);
    }

    public void checkSchemaRegistry() {
        log.info("Start checking schema registry...");
        int retryCount = 1;
        Integer maxRetry = retryConfigData.getMaxAttempts();
        Integer multiplier = retryConfigData.getBackOffMultiplier().intValue();
        Long sleepTimeMs = retryConfigData.getSleepTimeMs();
        boolean connecting = true;

        while (connecting) {
            try {
                connecting = !getSchemaRegistryStatus().is2xxSuccessful();
            } catch (Throwable t) {
                log.error("Error while checking schema registry", t);
                checkMaxRetry(retryCount++, maxRetry);
                sleep(sleepTimeMs);
                sleepTimeMs *= multiplier;
            }
            log.info("Check if schema-registry is available. Attempt: {}", retryCount);
            checkMaxRetry(retryCount++, maxRetry);
            sleep(sleepTimeMs);
            sleepTimeMs *= multiplier;
        }

        if (getSchemaRegistryStatus().is2xxSuccessful()) {
            log.info("Schema-registry is available! Attempt: {}", retryCount);
        }
    }

    private HttpStatus getSchemaRegistryStatus() {
        var httpStatusCode = webClient
                .get()
                .uri(kafkaConfigData.getSchemaRegistryUrl())
                .retrieve()
                .toBodilessEntity()
                .block()
                .getStatusCode();
        return HttpStatus.resolve(httpStatusCode.value());
    }

    public void checkTopicsCreated() {
        Collection<TopicListing> topics = getTopics();
        int retryCount = 1;
        Integer maxRetry = retryConfigData.getMaxAttempts();
        Integer multiplier = retryConfigData.getBackOffMultiplier().intValue();
        Long sleepTimeMs = retryConfigData.getSleepTimeMs();

        for (String topic : kafkaConfigData.getTopicNamesToCreate()) {
            while (!isTopicCreated(topics, topic)) {
                log.info("Check if topic {} created. Attempt: {}", topic, retryCount);
                checkMaxRetry(retryCount++, maxRetry);
                sleep(sleepTimeMs);
                sleepTimeMs *= multiplier;
                topics = getTopics();
            }
            if (isTopicCreated(topics, topic)) {
                log.info("Topic {} is now created with {} attempts!", topic, retryCount);
            }
        }
    }

    private boolean isTopicCreated(Collection<TopicListing> topics, String topicName) {
        if (topics == null) {
            return false;
        }
        return topics.stream().anyMatch(topic -> topic.name().equals(topicName));
    }

    private void checkMaxRetry(int retry, Integer maxRetry) {
        if (retry > maxRetry)
            throw new KafkaClientException("Reached max numbers of retry creating kafka topic(s)");
    }

    private void sleep(Long sleepTimeMs) {
        try {
            Thread.sleep(sleepTimeMs);
        } catch (InterruptedException e) {
            throw new KafkaClientException("Error while sleeping for waiting new created topics!");
        }
    }

    private Collection<TopicListing> getTopics() {
        Collection<TopicListing> topics;
        try {
            topics = retryTemplate.execute(this::doGetTopics);
        } catch (Throwable t) {
            throw new KafkaClientException("Reached max numbers of retry getting topic(s)", t);
        }
        return topics;
    }

    private Collection<TopicListing> doGetTopics(RetryContext retryContext)
            throws ExecutionException, InterruptedException {
        log.info("Reading kafka topic {}, attempt {}",
                kafkaConfigData.getTopicNamesToCreate().toArray(), retryContext.getRetryCount());
        Collection<TopicListing> topics = adminClient.listTopics().listings().get();
        if (topics != null) {
            topics.forEach(topic -> {
                log.info("Topic with name {} is ready", topic);
            });
        }
        return topics;
    }


}
