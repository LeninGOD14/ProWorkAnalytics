package com.prowork.analytics.repository;

import com.prowork.analytics.model.AsignacionTarea;
import com.prowork.analytics.model.Usuario;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface AsignacionTareaRepository extends JpaRepository<AsignacionTarea, Long> {

    List<AsignacionTarea> findByOperario(Usuario operario);

    List<AsignacionTarea> findByOperarioAndHoraFinIsNull(Usuario operario);

    List<AsignacionTarea> findAll();

    List<AsignacionTarea> findByHoraFinIsNotNull();

    List<AsignacionTarea> findByHoraFinIsNotNullAndHoraInicioBetween(
            LocalDateTime inicio, LocalDateTime fin
    );

    List<AsignacionTarea> findByFechaAsignacionBetween(
            LocalDate inicio, LocalDate fin
    );

}
