package com.prowork.analytics.dto;

public class KpiTiempoDTO {

    private String label;
    private double horas;
    private long tareas;
    private double eficiencia;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public double getHoras() {
        return horas;
    }

    public void setHoras(double horas) {
        this.horas = horas;
    }

    public long getTareas() {
        return tareas;
    }

    public void setTareas(long tareas) {
        this.tareas = tareas;
    }

    public double getEficiencia() {
        return eficiencia;
    }

    public void setEficiencia(double eficiencia) {
        this.eficiencia = eficiencia;
    }
    
    
    
}
