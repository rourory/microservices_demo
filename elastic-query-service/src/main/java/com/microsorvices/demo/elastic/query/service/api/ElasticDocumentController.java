package com.microsorvices.demo.elastic.query.service.api;

import com.microsorvices.demo.elastic.query.service.common.body.ElasticQueryServiceResponseModel;
import com.microsorvices.demo.elastic.query.service.common.body.ElasticServiceSearchRequestBody;
import com.microsorvices.demo.elastic.query.service.service.impl.MastodonElasticIndexService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@PreAuthorize("isAuthenticated()")
@RestController
@RequestMapping(value = "/documents", produces = "application/vnd.api.v1+json")
@Slf4j
public class ElasticDocumentController {

    private final MastodonElasticIndexService service;

    public ElasticDocumentController(MastodonElasticIndexService service) {
        this.service = service;
    }

    @PostAuthorize("hasPermission(returnObject, 'READ')")
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

    @PreAuthorize("hasPermission(#id, 'ElasticQueryServiceResponseModel', 'READ')")
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
    @PreAuthorize("hasRole('ROLE_APP_SUPERUSER_ROLE') || hasRole('ROLE_APP_ADMIN_ROLE')")
    @PostAuthorize("hasPermission(returnObject, 'READ')")
    @PostMapping("/search-by-text")
    public ResponseEntity<List<ElasticQueryServiceResponseModel>> getByText(@Valid @RequestBody ElasticServiceSearchRequestBody body) {
        return ResponseEntity.ok(service.findByTextContaining(body.getText()));
    }


}
