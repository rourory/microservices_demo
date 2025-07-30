package com.microsorvices.demo.elastic.index.client.service;

import com.microsorvices.demo.elastic.model.index.IndexModel;

import java.util.List;

public interface ElasticIndexClient<T extends IndexModel> {

    List<T> save(List<T> documents);

}
