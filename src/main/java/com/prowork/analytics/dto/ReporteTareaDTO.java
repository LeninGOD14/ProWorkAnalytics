package com.prowork.analytics.dto;

public class ReporteTareaDTO {

    private String operario;
    private String linea;
    private String tarea;
    private String dificultad;
    private int tiempoEstimado;
    private long tiempoReal;
    private long desviacion;

    public String getOperario() {
        return operario;
    }

    public void setOperario(String operario) {
        this.operario = operario;
    }

    public String getLinea() {
        return linea;
    }

    public void setLinea(String linea) {
        this.linea = linea;
    }

    public String getTarea() {
        return tarea;
    }

    public void setTarea(String tarea) {
        this.tarea = tarea;
    }

    public String getDificultad() {
        return dificultad;
    }

    public void setDificultad(String dificultad) {
        this.dificultad = dificultad;
    }

    public int getTiempoEstimado() {
        return tiempoEstimado;
    }

    public void setTiempoEstimado(int tiempoEstimado) {
        this.tiempoEstimado = tiempoEstimado;
    }

    public long getTiempoReal() {
        return tiempoReal;
    }

    public void setTiempoReal(long tiempoReal) {
        this.tiempoReal = tiempoReal;
    }

    public long getDesviacion() {
        return desviacion;
    }

    public void setDesviacion(long desviacion) {
        this.desviacion = desviacion;
    }
}
