package com.twelveshock.service.contract;

import com.twelveshock.dao.entity.ProgresoTarea;
import com.twelveshock.dto.ProgresoTareaDTO;
import com.twelveshock.dto.ResumenProgresoDTO;

import java.time.LocalDate;

public interface IProgresoTareaService {
    ProgresoTareaDTO obtenerProgresoPorFecha(LocalDate fecha);
    ProgresoTarea guardarProgreso(ProgresoTarea progresoTarea);
    ProgresoTarea actualizarProgreso(String id, ProgresoTarea progresoActualizado);
    boolean actualizarEstadoTarea(String idProgreso, String idTarea, int estado);
    ResumenProgresoDTO obtenerResumenProgreso(LocalDate fecha);
    ProgresoTarea inicializarProgresoDiario(LocalDate fecha);
}