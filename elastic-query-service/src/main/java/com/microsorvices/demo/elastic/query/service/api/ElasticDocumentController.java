package com.microsorvices.demo.elastic.query.service.api;

import com.microsorvices.demo.elastic.query.service.body.ElasticQueryServiceResponseModel;
import com.microsorvices.demo.elastic.query.service.service.impl.MastodonElasticIndexService;
import com.microsorvices.demo.elastic.query.service.body.ElasticServiceSearchRequestBody;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/documents")
@Slf4j
public class ElasticDocumentController {

    private final MastodonElasticIndexService service;

    public ElasticDocumentController(MastodonElasticIndexService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<ElasticQueryServiceResponseModel>> getAll() {
        log.info("Trying to find all the documents");
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ElasticQueryServiceResponseModel> getById(@NotEmpty @PathVariable String id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping("/search-by-text")
    public ResponseEntity<List<ElasticQueryServiceResponseModel>> getByText(@Valid @RequestBody ElasticServiceSearchRequestBody body) {
        return ResponseEntity.ok(service.findByTextContaining(body.getText()));
    }


}
