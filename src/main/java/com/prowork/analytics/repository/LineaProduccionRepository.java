package com.prowork.analytics.repository;

import com.prowork.analytics.model.LineaProduccion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LineaProduccionRepository extends JpaRepository<LineaProduccion, Long> {

    boolean existsByCodigo(String codigo);
}
