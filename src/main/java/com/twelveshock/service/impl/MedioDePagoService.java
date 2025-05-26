package com.twelveshock.service.impl;

import com.twelveshock.dao.entity.MedioDePago;
import com.twelveshock.repository.MedioDePagoRepository;
import com.twelveshock.service.contract.IMedioPagoService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.bson.types.ObjectId;

import java.util.List;

@ApplicationScoped
public class MedioDePagoService implements IMedioPagoService {

    @Inject
    MedioDePagoRepository medioDePagoRepository;

    public List<MedioDePago> obtenerMedios() {
        return medioDePagoRepository.listAll();
    }

    public MedioDePago guardarMedio(MedioDePago medio) {
        medioDePagoRepository.persist(medio);
        return medio;
    }

    public MedioDePago actualizarMedio(String id, MedioDePago medioActualizado) {
        MedioDePago medio = medioDePagoRepository.findById(new ObjectId(id));
        if (medio != null) {
            medio.setCodigo(medioActualizado.getCodigo());
            medio.setNombre(medioActualizado.getNombre());
            medio.setFechaCreacion(medioActualizado.getFechaCreacion());
            medioDePagoRepository.update(medio);
        }
        return medio;
    }

    public boolean eliminarMedio(String id) {
        MedioDePago medio = medioDePagoRepository.findById(new ObjectId(id));
        if (medio != null) {
            medioDePagoRepository.delete(medio);
            return true;
        }
        return false;
    }
}