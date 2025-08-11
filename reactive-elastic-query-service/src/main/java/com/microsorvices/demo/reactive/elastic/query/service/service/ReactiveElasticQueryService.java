package com.microsorvices.demo.reactive.elastic.query.service.service;

import com.microsorvices.demo.config.ElasticQueryServiceConfigData;
import com.microsorvices.demo.elastic.query.service.common.body.ElasticQueryServiceResponseModel;
import com.microsorvices.demo.elastic.query.service.common.transformer.ElasticToResponseModelTransformer;
import com.microsorvices.demo.reactive.elastic.query.service.repository.ReactiveElasticQueryRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.Duration;

@Service
public class ReactiveElasticQueryService {

    private final ReactiveElasticQueryRepository queryRepository;
    private final ElasticQueryServiceConfigData configData;
    private final ElasticToResponseModelTransformer transformer;

    public ReactiveElasticQueryService(ReactiveElasticQueryRepository queryRepository, ElasticQueryServiceConfigData configData, ElasticToResponseModelTransformer transformer) {
        this.queryRepository = queryRepository;
        this.configData = configData;
        this.transformer = transformer;
    }

    public Flux<ElasticQueryServiceResponseModel> findByText (String text){
        return queryRepository.findByText(text)
                .delayElements(Duration.ofMillis(configData.getBackPressureDelayMs()))
                .map(transformer::getResponseModel);
    }

}
