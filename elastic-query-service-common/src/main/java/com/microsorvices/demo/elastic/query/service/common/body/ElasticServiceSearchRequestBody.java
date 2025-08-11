package com.microsorvices.demo.elastic.query.service.common.body;


import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ElasticServiceSearchRequestBody {
    private String id;
    @NotEmpty
    private String text;
}
