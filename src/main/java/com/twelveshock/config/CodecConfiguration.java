package com.twelveshock.config;

import com.mongodb.MongoClientSettings;
import com.twelveshock.codec.LogProductCodecProvider;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.Produces;
import org.bson.codecs.configuration.CodecRegistry;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

@ApplicationScoped
public class CodecConfiguration {

    @Produces
    @ApplicationScoped
    public CodecRegistry codecRegistry() {
        return fromRegistries(
                MongoClientSettings.getDefaultCodecRegistry(),
                fromProviders(new LogProductCodecProvider())
        );
    }
}