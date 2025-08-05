package com.microsorvices.demo.elastic.query.service.body.assembler;

import com.microsorvices.demo.elastic.query.service.api.ElasticDocumentController;
import com.microsorvices.demo.elastic.query.service.body.ElasticQueryServiceResponseModel;
import com.microsorvices.demo.elastic.query.service.transformer.ElasticToResponseModelTransformer;
import com.microsorvices.demo.elastic.model.index.impl.MastodonIndexModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ElasticResponseModelAssembler extends RepresentationModelAssemblerSupport<MastodonIndexModel, ElasticQueryServiceResponseModel> {

    private final ElasticToResponseModelTransformer modelTransformer;

    public ElasticResponseModelAssembler(ElasticToResponseModelTransformer modelTransformer) {
        super(ElasticDocumentController.class, ElasticQueryServiceResponseModel.class);
        this.modelTransformer = modelTransformer;
    }

    @Override
    public ElasticQueryServiceResponseModel toModel(MastodonIndexModel indexModel) {
        ElasticQueryServiceResponseModel responseModel = modelTransformer.getResponseModel(indexModel);
        responseModel.add(
                linkTo(methodOn(ElasticDocumentController.class)
                        .getById(indexModel.getId()))
                        .withSelfRel()
        );

        responseModel.add(
                linkTo(ElasticDocumentController.class)
                        .withRel("documents")
        );
        return responseModel;
    }

    public List<ElasticQueryServiceResponseModel> toModels(List<MastodonIndexModel> indexModels) {
        return indexModels.stream().map(this::toModel).collect(Collectors.toList());
    }
}
