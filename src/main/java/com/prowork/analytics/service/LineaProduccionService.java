package com.prowork.analytics.service;

import com.prowork.analytics.model.LineaProduccion;
import com.prowork.analytics.repository.LineaProduccionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class LineaProduccionService {

    @Autowired
    private LineaProduccionRepository lineaProduccionRepository;

    public void crearLinea(LineaProduccion linea) {

        if (linea.getId() == null) {
            linea.setFechaCreacion(LocalDate.now());
        }

        lineaProduccionRepository.save(linea);
    }

    public List<LineaProduccion> listarLineas() {
        return lineaProduccionRepository.findAll();
    }

    public LineaProduccion buscarPorId(Long id) {
        return lineaProduccionRepository.findById(id).orElse(null);
    }

    public void eliminarLinea(Long id) {
        lineaProduccionRepository.deleteById(id);
    }
}
