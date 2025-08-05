package com.microsorvices.demo.elastic.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.microsorvices.demo.config.ElasticConfigData;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import java.net.URI;


@Configuration
@EnableElasticsearchRepositories(basePackages
        = "com.microsorvices.demo.elastic")
public class ElasticSearchConfig {

    private final ElasticConfigData elasticConfigData;

    public ElasticSearchConfig(ElasticConfigData elasticConfigData) {
        this.elasticConfigData = elasticConfigData;
    }

    @Bean
    public RestClient restClient() {
        var fullUri = URI.create(elasticConfigData.getConnectionUrl());
        return RestClient.builder(
                        new HttpHost(
                                fullUri.getHost(),
                                fullUri.getPort(),
                                fullUri.getScheme()))
                .build();
    }

    @Bean
    public ElasticsearchClient elasticsearchClient(RestClient restClient) {
        RestClientTransport transport = new RestClientTransport(
                restClient,
                new JacksonJsonpMapper()
        );
        return new ElasticsearchClient(transport);
    }

}
