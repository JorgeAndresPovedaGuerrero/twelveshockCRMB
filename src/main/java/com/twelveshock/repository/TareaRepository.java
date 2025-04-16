package com.twelveshock.repository;

import com.twelveshock.dao.entity.Tarea;
import io.quarkus.mongodb.panache.PanacheMongoRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class TareaRepository implements PanacheMongoRepository<Tarea> {
    public List<Tarea> findActiveTasks() {
        return find("activa", true).list();
    }
}