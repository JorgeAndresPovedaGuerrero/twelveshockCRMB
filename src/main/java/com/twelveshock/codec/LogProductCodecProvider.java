package com.twelveshock.codec;

import com.twelveshock.dao.entity.LogProduct;
import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;

public class LogProductCodecProvider implements CodecProvider {
    @Override
    public <T> Codec<T> get(Class<T> clazz, CodecRegistry registry) {
        if (clazz == LogProduct.class) {
            return (Codec<T>) new LogProductCodec(registry);
        }
        return null;
    }
}
