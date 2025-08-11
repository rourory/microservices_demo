package com.microsorvices.demo.elastic.query.service.common.transformer;

import com.microsorvices.demo.elastic.model.index.impl.MastodonIndexModel;
import com.microsorvices.demo.elastic.query.service.common.body.ElasticQueryServiceResponseModel;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ElasticToResponseModelTransformer {

    public ElasticQueryServiceResponseModel getResponseModel(MastodonIndexModel indexModel) {
        return ElasticQueryServiceResponseModel
                .builder()
                .id(indexModel.getId())
                .username(indexModel.getUsername())
                .text(indexModel.getText())
                .createdAt(indexModel.getCreatedAt())
                .build();
    }

    public List<ElasticQueryServiceResponseModel> getResponseModels(List<MastodonIndexModel> indexModels) {
        return indexModels.stream().map(this::getResponseModel).collect(Collectors.toList());
    }
}
