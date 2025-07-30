package com.microsorvices.demo.elastic.index.client.repository;

import com.microsorvices.demo.elastic.model.index.impl.MastodonIndexModel;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MastodonIndexRepository extends ElasticsearchRepository<MastodonIndexModel,String> {
    List<MastodonIndexModel> findMastodonIndexModelsByTextContaining(String text);
}
