package com.prowork.analytics.service;

import com.prowork.analytics.model.Asistencia;
import com.prowork.analytics.model.Usuario;
import com.prowork.analytics.repository.AsistenciaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class AsistenciaService {

    @Autowired
    private AsistenciaRepository asistenciaRepository;

    public Asistencia obtenerAsistenciaHoy(Usuario operario) {
        return asistenciaRepository
                .findByOperarioAndFecha(operario, LocalDate.now())
                .orElse(null);
    }

    public void marcarEntrada(Usuario operario) {
        Asistencia asistencia = obtenerAsistenciaHoy(operario);

        if (asistencia == null) {
            asistencia = new Asistencia();
            asistencia.setOperario(operario);
            asistencia.setFecha(LocalDate.now());
            asistencia.marcarEntrada();
            asistenciaRepository.save(asistencia);
        }
    }

    public void marcarSalida(Usuario operario) {
        Asistencia asistencia = obtenerAsistenciaHoy(operario);

        if (asistencia != null && asistencia.getHoraEntrada() != null
                && asistencia.getHoraSalida() == null) {

            asistencia.marcarSalida();
            asistenciaRepository.save(asistencia);
        }
    }

    public List<Asistencia> listarTodas() {
        return asistenciaRepository.findAll();
    }

    public List<Asistencia> listarPorOperario(Long operarioId) {
        return asistenciaRepository.findByOperarioId(operarioId);
    }

    public void eliminarPorId(Long id) {
        asistenciaRepository.deleteById(id);
    }

}
