package com.twelveshock.config;

import com.mongodb.MongoClientSettings;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Produces;
import org.bson.codecs.configuration.CodecRegistry;

@ApplicationScoped
public class MongoConfig {

    @Inject
    CodecRegistry codecRegistry;

    @Produces
    @ApplicationScoped
    public MongoClientSettings mongoClientSettings() {
        return MongoClientSettings.builder()
                .codecRegistry(codecRegistry)
                .build();
    }
}
