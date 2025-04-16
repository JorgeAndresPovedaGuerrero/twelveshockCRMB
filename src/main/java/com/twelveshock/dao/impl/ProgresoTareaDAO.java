package com.twelveshock.dao.impl;

import com.twelveshock.dao.entity.ProgresoTarea;
import com.twelveshock.dao.entity.Tarea;
import com.twelveshock.dto.ProgresoTareaDTO;
import com.twelveshock.dto.ResumenProgresoDTO;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

@ApplicationScoped
public class ProgresoTareaDAO {

    public ProgresoTareaDTO getProgresoByFecha(LocalDate fecha) {
        Optional<ProgresoTarea> progresoOpt = ProgresoTarea.find("fecha", fecha).firstResultOptional();

        if (progresoOpt.isPresent()) {
            ProgresoTarea progreso = progresoOpt.get();
            return new ProgresoTareaDTO(
                    progreso.id.toString(),
                    progreso.getFecha(),
                    progreso.getTareas()
            );
        }

        return null;
    }

    public ResumenProgresoDTO getResumenProgreso(LocalDate fecha) {
        Optional<ProgresoTarea> progresoOpt = ProgresoTarea.find("fecha", fecha).firstResultOptional();
        long totalTareasActivas = Tarea.count("activa", true);

        if (progresoOpt.isPresent()) {
            ProgresoTarea progreso = progresoOpt.get();
            Map<String, Integer> tareas = progreso.getTareas();

            // Contar tareas completadas (valor = 1)
            long tareasCompletadas = tareas.values().stream()
                    .filter(estado -> estado == 1)
                    .count();

            double porcentaje = totalTareasActivas > 0
                    ? (double) tareasCompletadas / totalTareasActivas * 100
                    : 0;

            return new ResumenProgresoDTO(
                    (int) totalTareasActivas,
                    (int) tareasCompletadas,
                    porcentaje
            );
        }

        // Si no hay progreso para la fecha, retornar 0 completadas
        return new ResumenProgresoDTO(
                (int) totalTareasActivas,
                0,
                0
        );
    }
}