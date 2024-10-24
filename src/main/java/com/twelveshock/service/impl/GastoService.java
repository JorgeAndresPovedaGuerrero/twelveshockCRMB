package com.twelveshock.service.impl;

import com.twelveshock.dao.entity.Gasto;
import com.twelveshock.repository.GastoRepository;
import com.twelveshock.service.contract.IGastoService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.bson.types.ObjectId;

import java.util.List;

@ApplicationScoped
public class GastoService implements IGastoService {

    @Inject
    GastoRepository gastoRepository;

    public List<Gasto> obtenerGastos() {
        return gastoRepository.listAll();
    }

    public Gasto guardarGasto(Gasto gasto) {
        gastoRepository.persist(gasto);
        return gasto;
    }

    public Gasto actualizarGasto(String id, Gasto gastoActualizado) {
        Gasto gasto = gastoRepository.findById(new ObjectId(id));
        if (gasto != null) {
            gasto.setFecha(gastoActualizado.getFecha());
            gasto.setValor(gastoActualizado.getValor());
            gasto.setConcepto(gastoActualizado.getConcepto());
            gastoRepository.update(gasto);
            return gasto;
        }
        return null;
    }

    public boolean eliminarGasto(String id) {
        return gastoRepository.deleteById(new ObjectId(id));
    }
}
