package com.microsorvices.demo.mastodontokafkaservice.service;

import com.google.gson.Gson;
import com.microsorvices.demo.mastodontokafkaservice.config.MastodonToKafkaServiceConfigData;
import com.microsorvices.demo.mastodontokafkaservice.entity.Toot;
import com.microsorvices.demo.mastodontokafkaservice.handler.MastodonHandler;
import com.microsorvices.demo.mastodontokafkaservice.handler.StandardMastodonHandler;
import com.sys1yagi.mastodon4j.MastodonClient;
import com.sys1yagi.mastodon4j.api.Shutdownable;
import com.sys1yagi.mastodon4j.api.method.Streaming;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class MastodonStreamService {

    private final Streaming streaming;
    private Shutdownable shutdownableStream;
    private final MastodonHandler mastodonHandler;

    public MastodonStreamService(MastodonToKafkaServiceConfigData configData) {
        MastodonClient client = new MastodonClient.Builder(configData.getInstance(), new OkHttpClient.Builder(), new Gson())
                .accessToken(configData.getAccessToken())
                .build();
        this.streaming = new Streaming(client);
        mastodonHandler = new StandardMastodonHandler();
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

    public List<Toot> getToots() {
        return mastodonHandler.getTootList(); // Возвращаем копию списка
    }
}
