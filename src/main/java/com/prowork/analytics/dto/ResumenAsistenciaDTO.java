package com.prowork.analytics.dto;

import java.time.Duration;

public class ResumenAsistenciaDTO {

    private Duration horasTotales;
    private Duration horasEfectivas;
    private Duration horasImproductivas;

    public ResumenAsistenciaDTO(Duration horasTotales,
                                Duration horasEfectivas,
                                Duration horasImproductivas) {
        this.horasTotales = horasTotales;
        this.horasEfectivas = horasEfectivas;
        this.horasImproductivas = horasImproductivas;
    }

    public Duration getHorasTotales() {
        return horasTotales;
    }

    public Duration getHorasEfectivas() {
        return horasEfectivas;
    }

    public Duration getHorasImproductivas() {
        return horasImproductivas;
    }
}
