package com.microsorvices.demo.mastodontokafka.service;

import com.google.gson.Gson;

import com.microsorvices.demo.config.MastodonToKafkaServiceConfigData;
import com.sys1yagi.mastodon4j.MastodonClient;
import com.sys1yagi.mastodon4j.api.Handler;
import com.sys1yagi.mastodon4j.api.Shutdownable;
import com.sys1yagi.mastodon4j.api.method.Streaming;


import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;
import java.util.List;

@Slf4j
@Service
public class MastodonStreamService {

    private final Streaming streaming;
    private Shutdownable shutdownableStream;
    private final Handler mastodonHandler;

    public MastodonStreamService(MastodonToKafkaServiceConfigData configData, Handler mastodonHandler) {
        this.mastodonHandler = mastodonHandler;
        MastodonClient client = new MastodonClient.Builder(configData.getInstance(), new OkHttpClient.Builder(), new Gson())
                .accessToken(configData.getAccessToken())
                .build();
        this.streaming = new Streaming(client);
    }

    public void startStreaming() {
        new Thread(() -> {
            try {
                shutdownableStream = streaming.localPublic(mastodonHandler);
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }).start();
    }

    @PreDestroy
    public void stopStreaming() {
        if (shutdownableStream != null) {
            shutdownableStream.shutdown();
            log.info("Shutting down streaming");
        }
    }
}
