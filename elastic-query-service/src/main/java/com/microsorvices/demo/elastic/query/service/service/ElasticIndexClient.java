package com.microsorvices.demo.elastic.query.service.service;


import com.microsorvices.demo.elastic.model.index.IndexModel;
import com.microsorvices.demo.elastic.query.service.common.body.ElasticQueryServiceResponseModel;

import java.util.List;

public interface ElasticIndexClient<T extends IndexModel, R extends ElasticQueryServiceResponseModel> {

    List<R> save(List<T> documents);
    List<R> findByTextContaining(String text);
    List<R> findAll();
    R findById(String id);
}
