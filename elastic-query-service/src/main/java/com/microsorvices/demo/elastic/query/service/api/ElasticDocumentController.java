package com.microsorvices.demo.elastic.query.service.api;

import com.microsorvices.demo.elastic.query.service.body.ElasticQueryServiceResponseModel;
import com.microsorvices.demo.elastic.query.service.service.impl.MastodonElasticIndexService;
import com.microsorvices.demo.elastic.query.service.body.ElasticServiceSearchRequestBody;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/documents", produces = "application/vnd.api.v1+json")
@Slf4j
public class ElasticDocumentController {

    private final MastodonElasticIndexService service;

    public ElasticDocumentController(MastodonElasticIndexService service) {
        this.service = service;
    }

    @Operation(summary = "Get all the documents from elasticsearch DB")
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = "Success",
                    content = {
                            @Content(mediaType = "application/vnd.api.v1+json",
                                    schema = @Schema(implementation = ElasticQueryServiceResponseModel.class))
                    }),
            @ApiResponse(responseCode = "400", description = "Not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
    })
    @GetMapping
    public ResponseEntity<List<ElasticQueryServiceResponseModel>> getAll() {
        log.info("Trying to find all the documents");
        return ResponseEntity.ok(service.findAll());
    }

    @Operation(summary = "Get document from Elasticsearch DB by document's ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = "Success",
                    content = {
                            @Content(mediaType = "application/vnd.api.v1+json",
                                    schema = @Schema(implementation = ElasticQueryServiceResponseModel.class))
                    }),
            @ApiResponse(responseCode = "400", description = "Not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
    })
    @GetMapping("/{id}")
    public ResponseEntity<ElasticQueryServiceResponseModel> getById(@NotEmpty @PathVariable String id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @Operation(summary = "Get document from Elasticsearch DB by document's text")
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = "Success",
                    content = {
                            @Content(mediaType = "application/vnd.api.v1+json",
                                    schema = @Schema(implementation = ElasticQueryServiceResponseModel.class))
                    }),
            @ApiResponse(responseCode = "400", description = "Not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
    })
    @PostMapping("/search-by-text")
    public ResponseEntity<List<ElasticQueryServiceResponseModel>> getByText(@Valid @RequestBody ElasticServiceSearchRequestBody body) {
        return ResponseEntity.ok(service.findByTextContaining(body.getText()));
    }


}
