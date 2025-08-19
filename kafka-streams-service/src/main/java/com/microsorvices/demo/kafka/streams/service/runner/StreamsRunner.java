package com.microsorvices.demo.kafka.streams.service.runner;

public interface StreamsRunner<K, V> {
    void start();
    default V getValueByKey(K key) {
        return null;
    }
}
