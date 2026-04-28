package com.prowork.analytics.repository;

import com.prowork.analytics.model.Asistencia;
import com.prowork.analytics.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface AsistenciaRepository extends JpaRepository<Asistencia, Long> {

    Optional<Asistencia> findByOperarioAndFecha(Usuario operario, LocalDate fecha);
    
    List<Asistencia> findByFechaBetween(LocalDate inicio, LocalDate fin);
    
    List<Asistencia> findByOperarioId(Long operarioId);

    
}
