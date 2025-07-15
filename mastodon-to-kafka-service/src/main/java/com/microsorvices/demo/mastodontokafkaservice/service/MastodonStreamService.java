package com.microsorvices.demo.mastodontokafkaservice.service;

import com.google.gson.Gson;
import com.microsorvices.demo.mastodontokafkaservice.config.MastodonToKafkaServiceConfigData;
import com.microsorvices.demo.mastodontokafkaservice.entity.Toot;
import com.sys1yagi.mastodon4j.MastodonClient;
import com.sys1yagi.mastodon4j.api.Handler;
import com.sys1yagi.mastodon4j.api.Shutdownable;
import com.sys1yagi.mastodon4j.api.entity.Status;
import com.sys1yagi.mastodon4j.api.method.Streaming;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
@Slf4j
public class MastodonStreamService {

    private final Streaming streaming;
    private final List<Toot> toots = new CopyOnWriteArrayList<>();
    private Shutdownable shutdownableStream;

    public MastodonStreamService(MastodonToKafkaServiceConfigData configData) {
        MastodonClient client = new MastodonClient.Builder(configData.getInstance(), new OkHttpClient.Builder(), new Gson())
                .accessToken(configData.getAccessToken())
                .build();
        this.streaming = new Streaming(client);
    }

    @PostConstruct
    public void startStreaming() {
        Handler handler = new Handler() {

            @Override
            public void onStatus(Status status) {
                Toot toot = new Toot();
                toot.setId(status.getId() + "");
                toot.setContent(status.getContent());
                if (status.getAccount() != null) {
                    toot.setUsername(status.getAccount().getUserName());
                } else {
                    toot.setUsername("UNKNOWN");
                }
                toot.setCreatedAt(status.getCreatedAt());
                toots.add(toot);
                log.info("New toot: {} by {} at {}", toot.getContent(), toot.getUsername(), status.getCreatedAt());
            }

            @Override
            public void onDelete(long l) {
                System.out.println("Deleted toot ID: " + l);
            }

            @Override
            public void onNotification(@NotNull com.sys1yagi.mastodon4j.api.entity.Notification notification) {
                // Ignore notifications
            }
        };

        new Thread(() -> {
            try {
                shutdownableStream = streaming.localPublic(handler);
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }).start();
    }

    @PreDestroy
    public void stopStreaming() {
        shutdownableStream.shutdown();
        log.info("Shutting down streaming");
    }

    public List<Toot> getToots() {
        var list = new ArrayList<>(toots);
        toots.clear();
        return list; // Возвращаем копию списка
    }
}
