package com.prowork.analytics.dto;

public class OperarioRendimientoDTO {

    private Long operarioId;
    private String nombreOperario;
    private double horasTotales;
    private double horasEfectivas;
    private double horasImproductivas;
    private double eficiencia;
    private long tareasCompletadas;
    private long tareasBaja;
    private long tareasMedia;
    private long tareasAlta;
    private String dificultadPredominante;

    public long getTareasBaja() {
        return tareasBaja;
    }

    public void setTareasBaja(long tareasBaja) {
        this.tareasBaja = tareasBaja;
    }

    public long getTareasMedia() {
        return tareasMedia;
    }

    public void setTareasMedia(long tareasMedia) {
        this.tareasMedia = tareasMedia;
    }

    public long getTareasAlta() {
        return tareasAlta;
    }

    public void setTareasAlta(long tareasAlta) {
        this.tareasAlta = tareasAlta;
    }

    public String getDificultadPredominante() {
        return dificultadPredominante;
    }

    public void setDificultadPredominante(String dificultadPredominante) {
        this.dificultadPredominante = dificultadPredominante;
    }

    public Long getOperarioId() {
        return operarioId;
    }

    public void setOperarioId(Long operarioId) {
        this.operarioId = operarioId;
    }

    public String getNombreOperario() {
        return nombreOperario;
    }

    public void setNombreOperario(String nombreOperario) {
        this.nombreOperario = nombreOperario;
    }

    public double getHorasTotales() {
        return horasTotales;
    }

    public void setHorasTotales(double horasTotales) {
        this.horasTotales = horasTotales;
    }

    public double getHorasEfectivas() {
        return horasEfectivas;
    }

    public void setHorasEfectivas(double horasEfectivas) {
        this.horasEfectivas = horasEfectivas;
    }

    public double getHorasImproductivas() {
        return horasImproductivas;
    }

    public void setHorasImproductivas(double horasImproductivas) {
        this.horasImproductivas = horasImproductivas;
    }

    public double getEficiencia() {
        return eficiencia;
    }

    public void setEficiencia(double eficiencia) {
        this.eficiencia = eficiencia;
    }

    public long getTareasCompletadas() {
        return tareasCompletadas;
    }

    public void setTareasCompletadas(long tareasCompletadas) {
        this.tareasCompletadas = tareasCompletadas;
    }
}
