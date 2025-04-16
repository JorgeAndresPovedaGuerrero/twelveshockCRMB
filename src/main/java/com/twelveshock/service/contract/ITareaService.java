package com.twelveshock.service.contract;

import com.twelveshock.dao.entity.Tarea;
import java.util.List;

public interface ITareaService {
    List<Tarea> obtenerTareas();
    List<Tarea> obtenerTareasActivas();
    Tarea guardarTarea(Tarea tarea);
    Tarea actualizarTarea(String id, Tarea tareaActualizada);
    boolean eliminarTarea(String id);
}