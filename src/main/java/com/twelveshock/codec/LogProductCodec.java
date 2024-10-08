package com.twelveshock.codec;

import com.twelveshock.dao.entity.LogProduct;
import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.Document;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bson.codecs.configuration.CodecRegistry;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LogProductCodec implements Codec<LogProduct> {
    private final Codec<Document> documentCodec;

    public LogProductCodec(CodecRegistry registry) {
        this.documentCodec = registry.get(Document.class);
    }

    @Override
    public LogProduct decode(BsonReader reader, DecoderContext decoderContext) {
        Document document = documentCodec.decode(reader, decoderContext);
        LogProduct logProduct = new LogProduct();

        // Decodificar los campos básicos
        logProduct.setTitle(document.getString("title"));
        logProduct.setOrderId(document.getLong("orderId"));

        // Manejar la fecha con conversión segura
        Object changeDateObj = document.get("changeDate");
        if (changeDateObj != null) {
            if (changeDateObj instanceof Date) {
                logProduct.setChangeDate(((Date) changeDateObj)
                        .toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDateTime());
            } else if (changeDateObj instanceof LocalDateTime) {
                logProduct.setChangeDate((LocalDateTime) changeDateObj);
            }
        }

        // Manejar el campo 'changes' que puede ser String o List<String>
        Object changesObj = document.get("changes");
        List<String> changesList = new ArrayList<>();

        if (changesObj instanceof String) {
            changesList.add((String) changesObj);
        } else if (changesObj instanceof List) {
            @SuppressWarnings("unchecked")
            List<String> list = (List<String>) changesObj;
            changesList.addAll(list);
        }

        logProduct.setChanges(changesList);

        return logProduct;
    }

    @Override
    public void encode(BsonWriter writer, LogProduct logProduct, EncoderContext encoderContext) {
        Document document = new Document();
        document.put("title", logProduct.getTitle());
        document.put("changes", logProduct.getChanges());

        // Convertir LocalDateTime a Date para MongoDB
        if (logProduct.getChangeDate() != null) {
            document.put("changeDate",
                    Date.from(logProduct.getChangeDate().atZone(ZoneId.systemDefault()).toInstant()));
        }

        document.put("orderId", logProduct.getOrderId());

        documentCodec.encode(writer, document, encoderContext);
    }

    @Override
    public Class<LogProduct> getEncoderClass() {
        return LogProduct.class;
    }
}
