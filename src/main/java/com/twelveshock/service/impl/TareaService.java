package com.twelveshock.service.impl;

import com.twelveshock.dao.entity.Tarea;
import com.twelveshock.repository.TareaRepository;
import com.twelveshock.service.contract.ITareaService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.bson.types.ObjectId;

import java.util.List;

@ApplicationScoped
public class TareaService implements ITareaService {

    @Inject
    TareaRepository tareaRepository;

    @Override
    public List<Tarea> obtenerTareas() {
        return tareaRepository.listAll();
    }

    @Override
    public List<Tarea> obtenerTareasActivas() {
        return tareaRepository.findActiveTasks();
    }

    @Override
    public Tarea guardarTarea(Tarea tarea) {
        tareaRepository.persist(tarea);
        return tarea;
    }

    @Override
    public Tarea actualizarTarea(String id, Tarea tareaActualizada) {
        Tarea tarea = tareaRepository.findById(new ObjectId(id));
        if (tarea != null) {
            tarea.setNombre(tareaActualizada.getNombre());
            tarea.setDescripcion(tareaActualizada.getDescripcion());
            tarea.setActiva(tareaActualizada.getActiva());
            tareaRepository.update(tarea);
            return tarea;
        }
        return null;
    }

    @Override
    public boolean eliminarTarea(String id) {
        return tareaRepository.deleteById(new ObjectId(id));
    }
}