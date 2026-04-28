package com.prowork.analytics.service;

import com.prowork.analytics.dto.ResumenAsistenciaDTO;
import com.prowork.analytics.model.AsignacionTarea;
import com.prowork.analytics.model.Asistencia;
import com.prowork.analytics.repository.AsignacionTareaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;

@Service
public class AsistenciaCalculoService {

    @Autowired
    private AsignacionTareaRepository asignacionTareaRepository;

    public ResumenAsistenciaDTO calcularResumen(Asistencia asistencia) {

        if (asistencia == null
                || asistencia.getHoraEntrada() == null
                || asistencia.getHoraSalida() == null) {
            return null;
        }

        Duration horasTotales = Duration.between(
                asistencia.getHoraEntrada(),
                asistencia.getHoraSalida()
        );

        List<AsignacionTarea> tareas =
                asignacionTareaRepository.findByOperario(asistencia.getOperario());

        Duration horasEfectivas = tareas.stream()
                .filter(t -> t.getHoraInicio() != null && t.getHoraFin() != null)
                .map(t -> Duration.between(t.getHoraInicio(), t.getHoraFin()))
                .reduce(Duration.ZERO, Duration::plus);

        Duration horasImproductivas = horasTotales.minus(horasEfectivas);

        return new ResumenAsistenciaDTO(
                horasTotales,
                horasEfectivas,
                horasImproductivas
        );
    }
}
