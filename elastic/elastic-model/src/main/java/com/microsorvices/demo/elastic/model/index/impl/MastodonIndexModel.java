package com.microsorvices.demo.elastic.model.index.impl;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.microsorvices.demo.elastic.model.index.IndexModel;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.Instant;

@Data
@Builder
@Document(indexName = "mastodon-index")
public class MastodonIndexModel implements IndexModel  {

    @JsonProperty
    private String id;
    @JsonProperty
    private String username;
    @JsonProperty
    private String text;

    @Field(type = FieldType.Date)
    @JsonProperty
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Instant createdAt;
}
