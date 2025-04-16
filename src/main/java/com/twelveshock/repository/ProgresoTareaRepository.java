package com.twelveshock.repository;

import com.twelveshock.dao.entity.ProgresoTarea;
import com.twelveshock.dao.impl.ProgresoTareaDAO;
import com.twelveshock.dto.ProgresoTareaDTO;
import com.twelveshock.dto.ResumenProgresoDTO;
import io.quarkus.mongodb.panache.PanacheMongoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.time.LocalDate;
import java.util.Optional;

@ApplicationScoped
public class ProgresoTareaRepository implements PanacheMongoRepository<ProgresoTarea> {

    @Inject
    ProgresoTareaDAO progresoTareaDAO;

    public Optional<ProgresoTarea> findByFecha(LocalDate fecha) {
        return find("fecha", fecha).firstResultOptional();
    }

    public ProgresoTareaDTO obtenerProgresoPorFecha(LocalDate fecha) {
        return progresoTareaDAO.getProgresoByFecha(fecha);
    }

    public ResumenProgresoDTO obtenerResumenProgreso(LocalDate fecha) {
        return progresoTareaDAO.getResumenProgreso(fecha);
    }
}