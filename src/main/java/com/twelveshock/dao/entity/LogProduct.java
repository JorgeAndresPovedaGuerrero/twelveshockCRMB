package com.twelveshock.dao.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.quarkus.mongodb.panache.PanacheMongoEntity;
import io.quarkus.mongodb.panache.common.MongoEntity;
import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@RegisterForReflection
@MongoEntity(collection = "LogProduct")
public class LogProduct extends PanacheMongoEntity {

    @JsonProperty("title")
    private String title;

    @JsonProperty("changes")
    private List<String> changes = new ArrayList<>();

    @JsonProperty("changeDate")
    private LocalDateTime changeDate;

    @JsonProperty("orderId")
    private long orderId;

    // Método para establecer los cambios de manera segura
    public void setChanges(Object changesObj) {
        if (changesObj == null) {
            this.changes = new ArrayList<>();
        } else if (changesObj instanceof String) {
            this.changes = new ArrayList<>();
            this.changes.add((String) changesObj);
        } else if (changesObj instanceof List) {
            @SuppressWarnings("unchecked")
            List<String> list = (List<String>) changesObj;
            this.changes = new ArrayList<>(list);
        }
    }

    // Método para establecer la fecha de manera segura
    public void setChangeDate(LocalDateTime date) {
        this.changeDate = date != null ? date : LocalDateTime.now();
    }
}