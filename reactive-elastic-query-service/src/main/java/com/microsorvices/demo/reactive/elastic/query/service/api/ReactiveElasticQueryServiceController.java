package com.microsorvices.demo.reactive.elastic.query.service.api;

import com.microsorvices.demo.elastic.query.service.common.body.ElasticQueryServiceResponseModel;
import com.microsorvices.demo.elastic.query.service.common.body.ElasticServiceSearchRequestBody;
import com.microsorvices.demo.reactive.elastic.query.service.service.ReactiveElasticQueryService;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RequestMapping("/documents")
@RestController
public class ReactiveElasticQueryServiceController {

    private final ReactiveElasticQueryService service;

    public ReactiveElasticQueryServiceController(ReactiveElasticQueryService service) {
        this.service = service;
    }

    @PostMapping(value = "/get-doc-by-text",
            produces = MediaType.TEXT_EVENT_STREAM_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public Flux<ElasticQueryServiceResponseModel> getDocsByText(
            @RequestBody @Valid ElasticServiceSearchRequestBody body) {
        return service.findByText(body.getText()).log();
    }

}























