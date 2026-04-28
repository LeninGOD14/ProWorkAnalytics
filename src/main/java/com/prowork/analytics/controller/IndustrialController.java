package com.prowork.analytics.controller;

import com.prowork.analytics.dto.KpiGeneralDTO;
import com.prowork.analytics.dto.OperarioRendimientoDTO;
import com.prowork.analytics.dto.KpiTiempoDTO;
import com.prowork.analytics.dto.ReporteTareaDTO;
import com.prowork.analytics.service.DashboardService;
import com.prowork.analytics.service.ReportesService;
import com.prowork.analytics.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/industrial")
public class IndustrialController {

    @Autowired
    private DashboardService dashboardService;

    @Autowired
    private ReportesService reportesService;

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/dashboard")
    public String verDashboard(Model model) {

        KpiGeneralDTO kpis = dashboardService.obtenerKpisGenerales();
        model.addAttribute("kpis", kpis);

        List<OperarioRendimientoDTO> operarios
                = dashboardService.obtenerRendimientoPorOperario();
        model.addAttribute("operarios", operarios);

        return "admin-dashboard";
    }

    @GetMapping("/reportes")
    public String verReportes(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin,
            @RequestParam(required = false) Long operarioId,
            Model model) {

        if (fechaInicio == null || fechaFin == null) {
            fechaFin = LocalDate.now();
            fechaInicio = fechaFin.minusDays(6);
        }

        List<ReporteTareaDTO> reporteTareas
                = reportesService.obtenerReporteTareas(fechaInicio, fechaFin, operarioId);

        List<KpiTiempoDTO> kpis
                = reportesService.obtenerKpisDiarios(fechaInicio, fechaFin, operarioId);

        model.addAttribute("reporteTareas", reporteTareas);
        model.addAttribute("labels", kpis.stream().map(KpiTiempoDTO::getLabel).toList());
        model.addAttribute("horasData", kpis.stream().map(KpiTiempoDTO::getHoras).toList());
        model.addAttribute("tareasData", kpis.stream().map(KpiTiempoDTO::getTareas).toList());
        model.addAttribute("eficienciaData", kpis.stream().map(KpiTiempoDTO::getEficiencia).toList());

        model.addAttribute("fechaInicio", fechaInicio);
        model.addAttribute("fechaFin", fechaFin);
        model.addAttribute("operarios", usuarioService.listarOperarios());
        model.addAttribute("operarioSeleccionado", operarioId);

        return "admin-reportes";
    }
}
