package com.microsorvices.demo.mastodontokafka.transformer;

import com.microservices.demo.kafka.avro.model.MastodonTootAvroModel;
import com.sys1yagi.mastodon4j.api.entity.Status;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@Slf4j
public class MastodonAvroTransformer {

    public MastodonTootAvroModel transformFrom(Status status) {
        long createdAt;
        try {
            createdAt = Instant.parse(status.getCreatedAt()).toEpochMilli();
        } catch (Exception e) {
            createdAt = Instant.now().toEpochMilli();
        }
        var builder = MastodonTootAvroModel.newBuilder()
                .setId(status.getId())
                .setContent(status.getContent())
                .setCreatedAt(createdAt);
        if (status.getAccount() != null) {
            builder.setUsername(status.getAccount().getUserName());
        } else {
            builder.setUsername("UNKNOWN");
        }

        return builder.build();
    }

}
