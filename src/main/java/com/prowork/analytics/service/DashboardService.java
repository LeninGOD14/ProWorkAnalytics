package com.prowork.analytics.service;

import com.prowork.analytics.dto.KpiGeneralDTO;
import com.prowork.analytics.dto.OperarioRendimientoDTO;
import com.prowork.analytics.model.AsignacionTarea;
import com.prowork.analytics.model.Asistencia;
import com.prowork.analytics.model.Dificultad;
import com.prowork.analytics.model.Usuario;
import com.prowork.analytics.repository.AsignacionTareaRepository;
import com.prowork.analytics.repository.AsistenciaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DashboardService {

    @Autowired
    private AsistenciaRepository asistenciaRepository;

    @Autowired
    private AsignacionTareaRepository asignacionTareaRepository;

    public KpiGeneralDTO obtenerKpisGenerales() {

        KpiGeneralDTO kpi = new KpiGeneralDTO();

        double horasTotales = asistenciaRepository.findAll().stream()
                .filter(a -> a.getHoraEntrada() != null && a.getHoraSalida() != null)
                .mapToDouble(a -> calcularHoras(a.getHoraEntrada(), a.getHoraSalida()))
                .sum();

        List<AsignacionTarea> tareasFinalizadas = asignacionTareaRepository.findAll().stream()
                .filter(t -> t.getHoraInicio() != null && t.getHoraFin() != null)
                .toList();

        double horasEfectivas = tareasFinalizadas.stream()
                .mapToDouble(t -> calcularHoras(t.getHoraInicio(), t.getHoraFin()))
                .sum();

        long tareasCompletadas = tareasFinalizadas.size();

        double horasImproductivas = Math.max(0, horasTotales - horasEfectivas);

        double eficiencia = (horasTotales > 0)
                ? (horasEfectivas / horasTotales) * 100
                : 0;

        kpi.setHorasTotales(redondear(horasTotales));
        kpi.setHorasEfectivas(redondear(horasEfectivas));
        kpi.setHorasImproductivas(redondear(horasImproductivas));
        kpi.setTareasCompletadas(tareasCompletadas);
        kpi.setEficiencia(redondear(eficiencia));

        return kpi;
    }

    public List<OperarioRendimientoDTO> obtenerRendimientoPorOperario() {

        List<OperarioRendimientoDTO> resultado = new ArrayList<>();

        Map<Usuario, List<Asistencia>> asistenciasPorOperario
                = asistenciaRepository.findAll().stream()
                        .filter(a -> a.getOperario() != null)
                        .collect(Collectors.groupingBy(Asistencia::getOperario));

        Map<Usuario, List<AsignacionTarea>> tareasPorOperario
                = asignacionTareaRepository.findAll().stream()
                        .filter(t -> t.getOperario() != null)
                        .collect(Collectors.groupingBy(AsignacionTarea::getOperario));

        for (Usuario operario : asistenciasPorOperario.keySet()) {

            OperarioRendimientoDTO dto = new OperarioRendimientoDTO();
            dto.setOperarioId(operario.getId());
            dto.setNombreOperario(operario.getUsername());

            double horasTotales = asistenciasPorOperario.get(operario).stream()
                    .filter(a -> a.getHoraEntrada() != null && a.getHoraSalida() != null)
                    .mapToDouble(a -> calcularHoras(a.getHoraEntrada(), a.getHoraSalida()))
                    .sum();

            List<AsignacionTarea> tareas
                    = tareasPorOperario.getOrDefault(operario, List.of());

            double horasEfectivas = tareas.stream()
                    .filter(t -> t.getHoraInicio() != null && t.getHoraFin() != null)
                    .mapToDouble(t -> calcularHoras(t.getHoraInicio(), t.getHoraFin()))
                    .sum();

            long tareasCompletadas = tareas.stream()
                    .filter(t -> t.getHoraFin() != null)
                    .count();

            double horasImproductivas = Math.max(0, horasTotales - horasEfectivas);

            double eficiencia = (horasTotales > 0)
                    ? (horasEfectivas / horasTotales) * 100
                    : 0;
            long tareasBaja = 0;
            long tareasMedia = 0;
            long tareasAlta = 0;

            for (AsignacionTarea asignacion : tareas) {

                if (asignacion.getHoraFin() != null
                        && asignacion.getTarea() != null
                        && asignacion.getTarea().getDificultad() != null) {

                    Dificultad dificultad = asignacion.getTarea().getDificultad();

                    switch (dificultad) {
                        case BAJA ->
                            tareasBaja++;
                        case MEDIA ->
                            tareasMedia++;
                        case ALTA ->
                            tareasAlta++;
                    }
                }
            }

            String dificultadPredominante;

            if (tareasAlta >= tareasMedia && tareasAlta >= tareasBaja) {
                dificultadPredominante = "ALTA";
            } else if (tareasMedia >= tareasBaja) {
                dificultadPredominante = "MEDIA";
            } else {
                dificultadPredominante = "BAJA";
            }

            dto.setHorasTotales(redondear(horasTotales));
            dto.setHorasEfectivas(redondear(horasEfectivas));
            dto.setHorasImproductivas(redondear(horasImproductivas));
            dto.setTareasCompletadas(tareasCompletadas);
            dto.setEficiencia(redondear(eficiencia));

            dto.setTareasBaja(tareasBaja);
            dto.setTareasMedia(tareasMedia);
            dto.setTareasAlta(tareasAlta);
            dto.setDificultadPredominante(dificultadPredominante);

            resultado.add(dto);
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
