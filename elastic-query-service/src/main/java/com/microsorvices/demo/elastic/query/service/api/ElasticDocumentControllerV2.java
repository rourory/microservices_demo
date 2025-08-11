package com.microsorvices.demo.elastic.query.service.api;

import com.microsorvices.demo.elastic.query.service.body.ElasticQueryServiceResponseModelV2;
import com.microsorvices.demo.elastic.query.service.common.body.ElasticQueryServiceResponseModel;
import com.microsorvices.demo.elastic.query.service.service.impl.MastodonElasticIndexService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.constraints.NotEmpty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/documents", produces = "application/vnd.api.v2+json")
@Slf4j
public class ElasticDocumentControllerV2 {

    private final MastodonElasticIndexService service;

    public ElasticDocumentControllerV2(MastodonElasticIndexService service) {
        this.service = service;
    }

    @Operation(summary = "Get all the documents from elasticsearch DB")
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = "Success",
                    content = {
                            @Content(mediaType = "application/vnd.api.v2+json",
                                    schema = @Schema(implementation = ElasticQueryServiceResponseModelV2.class))
                    }),
            @ApiResponse(responseCode = "400", description = "Not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
    })
    @GetMapping
    public ResponseEntity<List<ElasticQueryServiceResponseModelV2>> getAll() {
        log.info("Trying to find all the documents");
        return ResponseEntity.ok(service.findAll().stream().map(this::toV2Model).toList());
    }

    @Operation(summary = "Get document from elasticsearch DB by document's ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = "Success",
                    content = {
                            @Content(mediaType = "application/vnd.api.v2+json",
                                    schema = @Schema(implementation = ElasticQueryServiceResponseModelV2.class))
                    }),
            @ApiResponse(responseCode = "400", description = "Not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
    })
    @GetMapping("/{id}")
    public ResponseEntity<ElasticQueryServiceResponseModelV2> getById(@NotEmpty @PathVariable String id) {
        return ResponseEntity.ok(toV2Model(service.findById(id)));
    }


    private ElasticQueryServiceResponseModelV2 toV2Model(ElasticQueryServiceResponseModel v1Model) {
        var v2Model = ElasticQueryServiceResponseModelV2.builder()
                .id(Long.parseLong(v1Model.getId()))
                .username(v1Model.getUsername())
                .text(v1Model.getText())
                .textV2(v1Model.getText().toLowerCase())
                .build();
        return v2Model.add(v1Model.getLinks());

    }


}
