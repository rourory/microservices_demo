package com.microsorvices.demo.elastic.index.client.service.impl;

import com.microsorvices.demo.elastic.index.client.repository.MastodonIndexRepository;
import com.microsorvices.demo.elastic.index.client.service.ElasticIndexClient;
import com.microsorvices.demo.elastic.model.index.impl.MastodonIndexModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

import java.util.List;


@Service
@Slf4j
public class MastodonElasticIndexService implements ElasticIndexClient<MastodonIndexModel> {

    private final MastodonIndexRepository mastodonIndexRepository;

    public MastodonElasticIndexService(MastodonIndexRepository mastodonIndexRepository) {
        this.mastodonIndexRepository = mastodonIndexRepository;
    }

    @Override
    public List<MastodonIndexModel> save(List<MastodonIndexModel> documents) {
        List<MastodonIndexModel> result = new ArrayList<>();
        mastodonIndexRepository.saveAll(documents).forEach(result::add);
        return result;
    }

    public List<MastodonIndexModel> findByTextContaining(String text) {
        return mastodonIndexRepository.findMastodonIndexModelsByTextContaining(text);
    }


}
