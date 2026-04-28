package com.prowork.analytics.service;

import com.prowork.analytics.dto.KpiTiempoDTO;
import com.prowork.analytics.dto.ReporteTareaDTO;
import com.prowork.analytics.model.AsignacionTarea;
import com.prowork.analytics.model.Asistencia;
import com.prowork.analytics.repository.AsignacionTareaRepository;
import com.prowork.analytics.repository.AsistenciaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReportesService {

    @Autowired
    private AsignacionTareaRepository asignacionTareaRepository;

    @Autowired
    private AsistenciaRepository asistenciaRepository;

    public List<ReporteTareaDTO> obtenerReporteTareas(LocalDate inicio,
            LocalDate fin,
            Long operarioId) {

        List<AsignacionTarea> tareas = asignacionTareaRepository
                .findByFechaAsignacionBetween(inicio, fin).stream()
                .filter(t -> t.getHoraInicio() != null && t.getHoraFin() != null)
                .toList();

        if (operarioId != null) {
            tareas = tareas.stream()
                    .filter(t -> t.getOperario().getId().equals(operarioId))
                    .toList();
        }

        List<ReporteTareaDTO> resultado = new ArrayList<>();

        for (AsignacionTarea t : tareas) {

            ReporteTareaDTO dto = new ReporteTareaDTO();
            dto.setOperario(t.getOperario().getUsername());
            dto.setLinea(t.getLinea().getNombre());
            dto.setTarea(t.getTarea().getNombre());
            dto.setDificultad(t.getTarea().getDificultad().name());
            dto.setTiempoEstimado(t.getTiempoEstimadoMin());
            dto.setTiempoReal(t.getTiempoRealMin());
            dto.setDificultad(t.getTarea().getDificultad().name());

            long desviacion = t.getTiempoRealMin() - t.getTiempoEstimadoMin();
            dto.setDesviacion(desviacion);

            resultado.add(dto);
        }

        return resultado;
    }

    public List<KpiTiempoDTO> obtenerKpisDiarios(LocalDate inicio,
            LocalDate fin,
            Long operarioId) {

        List<Asistencia> asistencias = asistenciaRepository.findByFechaBetween(inicio, fin);
        List<AsignacionTarea> tareas = asignacionTareaRepository.findByFechaAsignacionBetween(inicio, fin);

        if (operarioId != null) {
            asistencias = asistencias.stream()
                    .filter(a -> a.getOperario().getId().equals(operarioId))
                    .toList();

            tareas = tareas.stream()
                    .filter(t -> t.getOperario().getId().equals(operarioId))
                    .toList();
        }

        Map<LocalDate, List<Asistencia>> asistenciasPorDia
                = asistencias.stream().collect(Collectors.groupingBy(Asistencia::getFecha));

        Map<LocalDate, List<AsignacionTarea>> tareasPorDia
                = tareas.stream().collect(Collectors.groupingBy(AsignacionTarea::getFechaAsignacion));

        List<KpiTiempoDTO> resultado = new ArrayList<>();

        LocalDate fecha = inicio;
        while (!fecha.isAfter(fin)) {

            List<Asistencia> aDia = asistenciasPorDia.getOrDefault(fecha, List.of());
            List<AsignacionTarea> tDia = tareasPorDia.getOrDefault(fecha, List.of());

            double horasTotales = aDia.stream()
                    .filter(a -> a.getHoraEntrada() != null && a.getHoraSalida() != null)
                    .mapToDouble(a -> calcularHoras(a.getHoraEntrada(), a.getHoraSalida()))
                    .sum();

            double horasEfectivas = tDia.stream()
                    .filter(t -> t.getHoraInicio() != null && t.getHoraFin() != null)
                    .mapToDouble(t -> calcularHoras(t.getHoraInicio(), t.getHoraFin()))
                    .sum();

            long tareasCompletadas = tDia.stream()
                    .filter(t -> t.getHoraFin() != null)
                    .count();

            double eficiencia = (horasTotales > 0)
                    ? (horasEfectivas / horasTotales) * 100
                    : 0;

            KpiTiempoDTO dto = new KpiTiempoDTO();
            dto.setLabel(fecha.toString());
            dto.setHoras(redondear(horasEfectivas));
            dto.setTareas(tareasCompletadas);
            dto.setEficiencia(redondear(eficiencia));

            resultado.add(dto);

            fecha = fecha.plusDays(1);
        }

        return resultado;
    }

    private double calcularHoras(LocalDateTime inicio, LocalDateTime fin) {
        return Duration.between(inicio, fin).toMinutes() / 60.0;
    }

    private double redondear(double valor) {
        return Math.round(valor * 100.0) / 100.0;
    }
}
