package com.microsorvices.demo.elastic.query.service.service.impl;


import com.microsorvices.demo.elastic.index.client.exception.ElasticSearchClientException;
import com.microsorvices.demo.elastic.index.client.repository.MastodonIndexRepository;
import com.microsorvices.demo.elastic.model.index.impl.MastodonIndexModel;
import com.microsorvices.demo.elastic.query.service.body.ElasticQueryServiceResponseModel;
import com.microsorvices.demo.elastic.query.service.body.assembler.ElasticResponseModelAssembler;
import com.microsorvices.demo.elastic.query.service.service.ElasticIndexClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@Slf4j
public class MastodonElasticIndexService implements ElasticIndexClient<MastodonIndexModel, ElasticQueryServiceResponseModel> {

    private final MastodonIndexRepository mastodonIndexRepository;
    private final ElasticResponseModelAssembler responseModelAssembler;

    public MastodonElasticIndexService(MastodonIndexRepository mastodonIndexRepository,
                                       ElasticResponseModelAssembler responseModelAssembler) {
        this.mastodonIndexRepository = mastodonIndexRepository;
        this.responseModelAssembler = responseModelAssembler;
    }

    @Override
    public List<ElasticQueryServiceResponseModel> save(List<MastodonIndexModel> documents) {
        List<MastodonIndexModel> result = new ArrayList<>();
        mastodonIndexRepository.saveAll(documents).forEach(result::add);
        return responseModelAssembler.toModels(result);
    }

    @Override
    public List<ElasticQueryServiceResponseModel> findByTextContaining(String text) {
        return responseModelAssembler.toModels(mastodonIndexRepository.findMastodonIndexModelsByTextContaining(text));
    }

    @Override
    public List<ElasticQueryServiceResponseModel> findAll() {
        List<MastodonIndexModel> result = new ArrayList<>();
        mastodonIndexRepository.findAll().forEach(result::add);
        return responseModelAssembler.toModels(result);
    }

    @Override
    public ElasticQueryServiceResponseModel findById(String id) throws ElasticSearchClientException {
        Optional<MastodonIndexModel> found = mastodonIndexRepository.findById(id);
        if (found.isPresent()) return responseModelAssembler.toModel(found.get());
        throw new ElasticSearchClientException("Could not find mastodon index with id: " + id);
    }


}
