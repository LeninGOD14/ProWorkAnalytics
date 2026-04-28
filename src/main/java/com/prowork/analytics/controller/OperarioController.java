package com.prowork.analytics.controller;

import com.prowork.analytics.model.AsignacionTarea;
import com.prowork.analytics.model.Usuario;
import com.prowork.analytics.repository.UsuarioRepository;
import com.prowork.analytics.service.AsignacionTareaService;
import com.prowork.analytics.service.AsistenciaCalculoService;
import com.prowork.analytics.service.AsistenciaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/operario")
public class OperarioController {

    @Autowired
    private AsignacionTareaService asignacionTareaService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private AsistenciaService asistenciaService;

    @Autowired
    private AsistenciaCalculoService asistenciaCalculoService;
    

    @GetMapping
    public String panelOperario() {
        return "operario";
    }


    @GetMapping("/tareas")
    public String verTareas(Model model, Authentication authentication) {

        Usuario operario = usuarioRepository
                .findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        List<AsignacionTarea> asignaciones =
                asignacionTareaService.obtenerTareasPorOperario(operario);

        model.addAttribute("asignaciones", asignaciones);
        return "operario-tareas";
    }

    
    @PostMapping("/tareas/iniciar/{id}")
    public String iniciarTarea(@PathVariable Long id) {
        asignacionTareaService.iniciarTarea(id);
        return "redirect:/operario/tareas";
    }

 
    @PostMapping("/tareas/finalizar/{id}")
    public String finalizarTarea(@PathVariable Long id) {
        asignacionTareaService.finalizarTarea(id);
        return "redirect:/operario/tareas";
    }


    @GetMapping("/asistencia/registrar")
    public String verAsistencia(Model model, Authentication authentication) {

        Usuario operario = usuarioRepository
                .findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        var asistencia = asistenciaService.obtenerAsistenciaHoy(operario);
        model.addAttribute("asistencia", asistencia);

        if (asistencia != null && asistencia.getHoraSalida() != null) {
            model.addAttribute(
                    "resumen",
                    asistenciaCalculoService.calcularResumen(asistencia)
            );
        }

        return "operario-asistencia";
    }

    @PostMapping("/asistencia/entrada")
    public String marcarEntrada(Authentication authentication) {

        Usuario operario = usuarioRepository
                .findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        asistenciaService.marcarEntrada(operario);
        return "redirect:/operario/asistencia/registrar";
    }

    @PostMapping("/asistencia/salida")
    public String marcarSalida(Authentication authentication) {

        Usuario operario = usuarioRepository
                .findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        asistenciaService.marcarSalida(operario);
        return "redirect:/operario/asistencia/registrar";
    }
}
