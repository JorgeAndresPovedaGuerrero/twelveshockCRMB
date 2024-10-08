package com.twelveshock.config;

import com.mongodb.MongoClientSettings;
import com.twelveshock.codec.LogProductCodec;
import com.twelveshock.codec.LogProductCodecProvider;
import com.twelveshock.dao.entity.LogProduct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.Produces;
import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecProvider;
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