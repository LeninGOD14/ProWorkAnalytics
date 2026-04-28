package com.prowork.analytics.controller;

import com.prowork.analytics.model.AsignacionTarea;
import com.prowork.analytics.model.LineaProduccion;
import com.prowork.analytics.model.Tarea;
import com.prowork.analytics.model.Usuario;
import com.prowork.analytics.service.AsignacionTareaService;
import com.prowork.analytics.service.AsistenciaService;
import com.prowork.analytics.service.LineaProduccionService;
import com.prowork.analytics.service.TareaService;
import com.prowork.analytics.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SupervisorController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private LineaProduccionService lineaProduccionService;

    @Autowired
    private TareaService tareaService;

    @Autowired
    private AsignacionTareaService asignacionTareaService;

    @Autowired
    private AsistenciaService asistenciaService;

    @GetMapping("/supervisor")
    public String panelSupervisor(Model model) {
        model.addAttribute("lineas", lineaProduccionService.listarLineas());
        return "supervisor";
    }

    @GetMapping("/supervisor/reportes")
    public String verReportes(Model model) {
        model.addAttribute("operarios", usuarioService.listarOperarios());
        return "supervisor-reportes";
    }

    @GetMapping("/supervisor/lineas")
    public String verLineas(Model model) {
        model.addAttribute("lineas", lineaProduccionService.listarLineas());
        return "supervisor-lineas";
    }

    @GetMapping("/supervisor/tareas/nuevo")
    public String nuevaAsignacion(Model model) {

        model.addAttribute("operarios", usuarioService.listarOperarios());
        model.addAttribute("lineas", lineaProduccionService.listarLineas());
        model.addAttribute("tareas", tareaService.listarTareas());
        model.addAttribute("asignacion", new AsignacionTarea());

        return "supervisor-asignar-tarea";
    }

    @PostMapping("/supervisor/tareas/guardar")
    public String guardarAsignacion(
            @RequestParam Long operarioId,
            @RequestParam Long lineaId,
            @RequestParam Long tareaId,
            @RequestParam Integer tiempoEstimadoMin
    ) {

        Usuario operario = usuarioService.buscarPorId(operarioId);
        LineaProduccion linea = lineaProduccionService.buscarPorId(lineaId);
        Tarea tarea = tareaService.buscarPorId(tareaId);

        AsignacionTarea asignacion = new AsignacionTarea();
        asignacion.setOperario(operario);
        asignacion.setLinea(linea);
        asignacion.setTarea(tarea);
        asignacion.setTiempoEstimadoMin(tiempoEstimadoMin);

        asignacionTareaService.asignarTarea(asignacion);

        return "redirect:/supervisor/tareas";
    }

    @GetMapping("/supervisor/tareas")
    public String verTareasAsignadas(Model model) {

        model.addAttribute(
                "asignaciones",
                asignacionTareaService.obtenerTodasLasAsignaciones()
        );

        return "supervisor-tareas";
    }

    @GetMapping("/supervisor/asignaciones/eliminar/{id}")
    public String eliminarAsignacion(@PathVariable Long id) {
        asignacionTareaService.eliminarPorId(id);
        return "redirect:/supervisor/tareas";
    }

    @GetMapping("/supervisor/asistencias")
    public String verAsistencias(
            @RequestParam(required = false) Long operarioId,
            Model model) {

        model.addAttribute("operarios", usuarioService.listarOperarios());

        if (operarioId != null) {
            model.addAttribute(
                    "asistencias",
                    asistenciaService.listarPorOperario(operarioId)
            );
            model.addAttribute("operarioSeleccionado", operarioId);
        } else {
            model.addAttribute(
                    "asistencias",
                    asistenciaService.listarTodas()
            );
        }

        return "supervisor-asistencias";
    }

    @GetMapping("/supervisor/asistencias/eliminar/{id}")
    public String eliminarAsistencia(@PathVariable Long id) {
        asistenciaService.eliminarPorId(id);
        return "redirect:/supervisor/asistencias";
    }

}
