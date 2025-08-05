package com.microsorvices.demo.elastic.index.client.exception;

public class ElasticSearchClientException extends RuntimeException{

    public ElasticSearchClientException() {
    }

    public ElasticSearchClientException(String message) {
        super(message);
    }

    public ElasticSearchClientException(String message, Throwable cause) {
        super(message, cause);
    }
}
