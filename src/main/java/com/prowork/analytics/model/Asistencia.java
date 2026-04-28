package com.prowork.analytics.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "asistencias",
       uniqueConstraints = @UniqueConstraint(columnNames = {"operario_id", "fecha"}))
public class Asistencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "operario_id", nullable = false)
    private Usuario operario;

    @Column(nullable = false)
    private LocalDate fecha;

    private LocalDateTime horaEntrada;
    private LocalDateTime horaSalida;

    public void marcarEntrada() {
        this.horaEntrada = LocalDateTime.now();
    }

    public void marcarSalida() {
        this.horaSalida = LocalDateTime.now();
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

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public LocalDateTime getHoraEntrada() {
        return horaEntrada;
    }

    public LocalDateTime getHoraSalida() {
        return horaSalida;
    }
}
