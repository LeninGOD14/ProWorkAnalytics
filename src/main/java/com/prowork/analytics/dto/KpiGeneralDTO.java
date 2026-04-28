package com.prowork.analytics.dto;

public class KpiGeneralDTO {

    private double horasTotales;
    private double horasEfectivas;
    private double horasImproductivas;
    private long tareasCompletadas;
    private double eficiencia;

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

    public long getTareasCompletadas() {
        return tareasCompletadas;
    }

    public void setTareasCompletadas(long tareasCompletadas) {
        this.tareasCompletadas = tareasCompletadas;
    }

    public double getEficiencia() {
        return eficiencia;
    }

    public void setEficiencia(double eficiencia) {
        this.eficiencia = eficiencia;
    }
}
