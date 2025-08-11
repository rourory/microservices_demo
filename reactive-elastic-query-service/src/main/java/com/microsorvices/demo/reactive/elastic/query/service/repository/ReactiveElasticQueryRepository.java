package com.microsorvices.demo.reactive.elastic.query.service.repository;

import com.microsorvices.demo.elastic.model.index.impl.MastodonIndexModel;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface ReactiveElasticQueryRepository extends ReactiveCrudRepository<MastodonIndexModel, String> {

    Flux<MastodonIndexModel> findByText(String text);

}
