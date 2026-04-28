package com.prowork.analytics.service;

import com.prowork.analytics.model.AsignacionTarea;
import com.prowork.analytics.model.Usuario;
import com.prowork.analytics.repository.AsignacionTareaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AsignacionTareaService {

    @Autowired
    private AsignacionTareaRepository asignacionTareaRepository;

    public List<AsignacionTarea> obtenerTareasPorOperario(Usuario operario) {
        return asignacionTareaRepository.findByOperario(operario);
    }

    public List<AsignacionTarea> obtenerTareasActivasPorOperario(Usuario operario) {
        return asignacionTareaRepository.findByOperarioAndHoraFinIsNull(operario);
    }

    public AsignacionTarea buscarPorId(Long id) {
        return asignacionTareaRepository.findById(id).orElse(null);
    }

    public void iniciarTarea(Long asignacionId) {
        AsignacionTarea asignacion = buscarPorId(asignacionId);
        if (asignacion != null && asignacion.getHoraInicio() == null) {
            asignacion.iniciarTarea();
            asignacionTareaRepository.save(asignacion);
        }
    }

    public void finalizarTarea(Long asignacionId) {
        AsignacionTarea asignacion = buscarPorId(asignacionId);
        if (asignacion != null && asignacion.getHoraInicio() != null
                && asignacion.getHoraFin() == null) {

            asignacion.finalizarTarea();
            asignacionTareaRepository.save(asignacion);
        }
    }

    public void asignarTarea(AsignacionTarea asignacion) {
        asignacionTareaRepository.save(asignacion);
    }

    public void eliminarPorId(Long id) {
        asignacionTareaRepository.deleteById(id);
    }

    public List<AsignacionTarea> obtenerTodasLasAsignaciones() {
        return asignacionTareaRepository.findAll();
    }

}
