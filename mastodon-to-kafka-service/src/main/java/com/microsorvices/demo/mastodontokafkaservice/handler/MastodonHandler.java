package com.microsorvices.demo.mastodontokafkaservice.handler;

import com.microsorvices.demo.mastodontokafkaservice.entity.Toot;
import com.sys1yagi.mastodon4j.api.Handler;

import java.util.List;

public interface MastodonHandler extends Handler {
    List<Toot> getTootList();
}
