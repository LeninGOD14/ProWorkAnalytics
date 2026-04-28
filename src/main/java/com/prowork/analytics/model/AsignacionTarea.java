package com.prowork.analytics.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Duration;

@Entity
@Table(name = "asignaciones_tareas")
public class AsignacionTarea {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "operario_id", nullable = false)
    private Usuario operario;

    @ManyToOne
    @JoinColumn(name = "linea_id", nullable = false)
    private LineaProduccion linea;

    @ManyToOne
    @JoinColumn(name = "tarea_id", nullable = false)
    private Tarea tarea;

    @Column(nullable = false)
    private Integer tiempoEstimadoMin;

    private LocalDateTime horaInicio;
    private LocalDateTime horaFin;

    private Long tiempoRealMin;

    private LocalDate fechaAsignacion = LocalDate.now();

    public void iniciarTarea() {
        this.horaInicio = LocalDateTime.now();
    }

    public void finalizarTarea() {
        this.horaFin = LocalDateTime.now();
        calcularTiempoReal();
    }

    private void calcularTiempoReal() {
        if (horaInicio != null && horaFin != null) {
            this.tiempoRealMin = Duration.between(horaInicio, horaFin).toMinutes();
        }   
    }
   
    public Long getId() {
        return id;
    }

    public Usuario getOperario() {
        return operario;
    }

    public void setOperario(Usuario operario) {
        this.operario = operario;
    }

    public LineaProduccion getLinea() {
        return linea;
    }

    public void setLinea(LineaProduccion linea) {
        this.linea = linea;
    }

    public Tarea getTarea() {
        return tarea;
    }

    public void setTarea(Tarea tarea) {
        this.tarea = tarea;
    }

    public Integer getTiempoEstimadoMin() {
        return tiempoEstimadoMin;
    }

    public void setTiempoEstimadoMin(Integer tiempoEstimadoMin) {
        this.tiempoEstimadoMin = tiempoEstimadoMin;
    }

    public LocalDateTime getHoraInicio() {
        return horaInicio;
    }

    public LocalDateTime getHoraFin() {
        return horaFin;
    }

    public Long getTiempoRealMin() {
        return tiempoRealMin;
    }

    public LocalDate getFechaAsignacion() {
        return fechaAsignacion;
    }
}
