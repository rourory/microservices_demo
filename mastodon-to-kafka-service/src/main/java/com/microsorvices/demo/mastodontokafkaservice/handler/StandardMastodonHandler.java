package com.microsorvices.demo.mastodontokafkaservice.handler;

import com.microsorvices.demo.mastodontokafkaservice.entity.Toot;
import com.sys1yagi.mastodon4j.api.Handler;
import com.sys1yagi.mastodon4j.api.entity.Status;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;


@Slf4j
public class StandardMastodonHandler implements MastodonHandler {
    private final List<Toot> toots = new CopyOnWriteArrayList<>();

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

    @Override
    public List<Toot> getTootList() {
        var list = new ArrayList<>(toots);
        toots.clear();
        return list;
    }
}
