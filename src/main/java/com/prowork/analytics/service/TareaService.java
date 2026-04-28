package com.prowork.analytics.service;

import com.prowork.analytics.model.Tarea;
import com.prowork.analytics.repository.TareaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import org.springframework.dao.DataIntegrityViolationException;

@Service
public class TareaService {

    @Autowired
    private TareaRepository tareaRepository;

    public List<Tarea> listarTareas() {
        return tareaRepository.findAll();
    }

    public Tarea buscarPorId(Long id) {
        return tareaRepository.findById(id).orElse(null);
    }

    public void guardar(Tarea tarea) {
        tareaRepository.save(tarea);
    }

    public void eliminar(Long id) {
        try {
            tareaRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException(
                    "No se puede eliminar la tarea porque ya está asociada a un operario."
            );
        }
    }
}
