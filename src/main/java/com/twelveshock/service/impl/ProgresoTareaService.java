package com.twelveshock.service.impl;

import com.twelveshock.dao.entity.ProgresoTarea;
import com.twelveshock.dao.entity.Tarea;
import com.twelveshock.dto.ProgresoTareaDTO;
import com.twelveshock.dto.ResumenProgresoDTO;
import com.twelveshock.repository.ProgresoTareaRepository;
import com.twelveshock.repository.TareaRepository;
import com.twelveshock.service.contract.IProgresoTareaService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.bson.types.ObjectId;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@ApplicationScoped
public class ProgresoTareaService implements IProgresoTareaService {

    @Inject
    ProgresoTareaRepository progresoTareaRepository;

    @Inject
    TareaRepository tareaRepository;

    @Override
    public ProgresoTareaDTO obtenerProgresoPorFecha(LocalDate fecha) {
        return progresoTareaRepository.obtenerProgresoPorFecha(fecha);
    }

    @Override
    public ProgresoTarea guardarProgreso(ProgresoTarea progresoTarea) {
        progresoTareaRepository.persist(progresoTarea);
        return progresoTarea;
    }

    @Override
    public ProgresoTarea actualizarProgreso(String id, ProgresoTarea progresoActualizado) {
        ProgresoTarea progreso = progresoTareaRepository.findById(new ObjectId(id));
        if (progreso != null) {
            progreso.setFecha(progresoActualizado.getFecha());
            progreso.setTareas(progresoActualizado.getTareas());
            progresoTareaRepository.update(progreso);
            return progreso;
        }
        return null;
    }

    @Override
    public boolean actualizarEstadoTarea(String idProgreso, String idTarea, int estado) {
        ProgresoTarea progreso = progresoTareaRepository.findById(new ObjectId(idProgreso));
        if (progreso != null) {
            Map<String, Integer> tareas = progreso.getTareas();
            tareas.put(idTarea, estado);
            progreso.setTareas(tareas);
            progresoTareaRepository.update(progreso);
            return true;
        }
        return false;
    }

    @Override
    public ResumenProgresoDTO obtenerResumenProgreso(LocalDate fecha) {
        return progresoTareaRepository.obtenerResumenProgreso(fecha);
    }

    @Override
    public ProgresoTarea inicializarProgresoDiario(LocalDate fecha) {
        // Verificar si ya existe un progreso para esta fecha
        Optional<ProgresoTarea> existente = progresoTareaRepository.findByFecha(fecha);
        if (existente.isPresent()) {
            return existente.get();
        }

        // Si no existe, crear uno nuevo con todas las tareas activas inicializadas a 0
        List<Tarea> tareasActivas = tareaRepository.findActiveTasks();
        Map<String, Integer> estadoTareas = new HashMap<>();

        for (Tarea tarea : tareasActivas) {
            estadoTareas.put(tarea.id.toString(), 0); // 0 = no completada
        }

        ProgresoTarea nuevaProgreso = new ProgresoTarea();
        nuevaProgreso.setFecha(fecha);
        nuevaProgreso.setTareas(estadoTareas);

        progresoTareaRepository.persist(nuevaProgreso);
        return nuevaProgreso;
    }
}