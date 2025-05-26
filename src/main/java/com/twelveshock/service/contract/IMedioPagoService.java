package com.twelveshock.service.contract;

import com.twelveshock.dao.entity.MedioDePago;

import java.util.List;

public interface IMedioPagoService {

    List<MedioDePago> obtenerMedios();

    MedioDePago guardarMedio(MedioDePago medio);

    MedioDePago actualizarMedio(String id, MedioDePago medioActualizado);

    boolean eliminarMedio(String id);
}