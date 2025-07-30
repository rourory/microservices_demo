package com.microsorvices.demo.elastic.config;

import com.microsorvices.demo.config.ElasticConfigData;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Objects;


@Configuration
@EnableElasticsearchRepositories(basePackages
        = "com.microsorvices.demo")
@ComponentScan(basePackages = {"com.microsorvices.demo"})
public class ElasticSearchConfig {

    private final ElasticConfigData elasticConfigData;

    public ElasticSearchConfig(ElasticConfigData elasticConfigData) {
        this.elasticConfigData = elasticConfigData;
    }

    @Bean
    public RestHighLevelClient restHighLevelClient() {
        return new RestHighLevelClient(RestClient.builder(elasticHttpHost())
                .setRequestConfigCallback(this::configureRequestConfigBuilder));
    }

    public HttpHost elasticHttpHost() {
        UriComponents serverUri = buildServerUri();
        return new HttpHost(
                Objects.requireNonNull(serverUri.getHost()),
                serverUri.getPort(),
                serverUri.getScheme()
        );
    }

    private UriComponents buildServerUri(){
        return UriComponentsBuilder.fromHttpUrl(elasticConfigData.getConnectionUrl()).build();
    }

    public RequestConfig.Builder configureRequestConfigBuilder(RequestConfig.Builder builder) {
        return builder.setSocketTimeout(elasticConfigData.getSocketTimeoutMs())
                .setConnectTimeout(elasticConfigData.getConnectTimeoutMs());
    }

}
