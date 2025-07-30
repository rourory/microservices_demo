package com.microsorvices.demo.kafka.to.elastic.service.mapper;

import com.microservices.demo.kafka.avro.model.MastodonTootAvroModel;
import com.microsorvices.demo.elastic.model.index.impl.MastodonIndexModel;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

@Component
public class MastodonAvroToElasticModelMapper {

    public List<MastodonIndexModel> getElasticModels(List<MastodonTootAvroModel> toots) {
        return toots.stream().map(toot -> MastodonIndexModel.builder()
                .id(String.valueOf(toot.getId()))
                .username(toot.getUsername())
                .text(toot.getContent())
                .createdAt(Instant.ofEpochMilli(toot.getCreatedAt()))
                .build()
        ).toList();
    }

}
