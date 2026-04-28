package com.prowork.analytics.controller;

import com.prowork.analytics.dto.KpiGeneralDTO;
import com.prowork.analytics.dto.OperarioRendimientoDTO;
import com.prowork.analytics.model.Usuario;
import com.prowork.analytics.model.LineaProduccion;
import com.prowork.analytics.service.UsuarioService;
import com.prowork.analytics.service.LineaProduccionService;
import com.prowork.analytics.service.DashboardService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes; // IMPORTANTE

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private LineaProduccionService lineaProduccionService;

    @Autowired
    private DashboardService dashboardService;

    @GetMapping
    public String adminPanel() {
        return "admin";
    }
    @GetMapping("/dashboard")
    public String verDashboard(Model model) {
        KpiGeneralDTO kpis = dashboardService.obtenerKpisGenerales();
        model.addAttribute("kpis", kpis);
        List<OperarioRendimientoDTO> operarios = dashboardService.obtenerRendimientoPorOperario();
        model.addAttribute("operarios", operarios);
        return "admin-dashboard";
    }

    @GetMapping("/supervisores")
    public String listarSupervisores(Model model) {
        model.addAttribute("supervisores", usuarioService.listarSupervisores());
        return "supervisor-list";
    }

    @GetMapping("/supervisores/nuevo")
    public String nuevoSupervisor(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "supervisor-form";
    }

    @PostMapping("/supervisores/guardar")
    public String guardarSupervisor(@ModelAttribute Usuario usuario, Model model, RedirectAttributes flash) {
        try {
            usuarioService.crearSupervisor(usuario);
            flash.addFlashAttribute("mensajeExito", "Supervisor guardado correctamente.");
            return "redirect:/admin/supervisores";
        } catch (IllegalArgumentException e) {
            if ("USERNAME_DUPLICADO".equals(e.getMessage())) {
                model.addAttribute("error", "⚠ Ya existe un supervisor con ese nombre de usuario.");
            } else {
                model.addAttribute("error", "❌ Error al guardar el supervisor.");
            }
            model.addAttribute("usuario", usuario);
            return "supervisor-form";
        }
    }

    @GetMapping("/supervisores/editar/{id}")
    public String editarSupervisor(@PathVariable Long id, Model model) {
        model.addAttribute("usuario", usuarioService.buscarPorId(id));
        return "supervisor-form";
    }

    @GetMapping("/supervisores/eliminar/{id}")
    public String eliminarSupervisor(@PathVariable Long id, RedirectAttributes flash) {
        try {
            usuarioService.eliminarUsuario(id);
            flash.addFlashAttribute("mensajeExito", "Supervisor eliminado exitosamente.");
        } catch (Exception e) {
            flash.addFlashAttribute("mensajeError", "No se puede eliminar: El supervisor tiene registros asociados.");
        }
        return "redirect:/admin/supervisores";
    }

    @GetMapping("/operarios")
    public String listarOperarios(Model model) {
        model.addAttribute("operarios", usuarioService.listarOperarios());
        return "operario-list";
    }

    @GetMapping("/operarios/nuevo")
    public String nuevoOperario(Model model) {
        model.addAttribute("operario", new Usuario());
        return "operario-form";
    }

    @PostMapping("/operarios/guardar")
    public String guardarOperario(@ModelAttribute Usuario usuario, Model model, RedirectAttributes flash) {
        try {
            usuarioService.crearOperario(usuario);
            flash.addFlashAttribute("mensajeExito", "Operario guardado correctamente.");
            return "redirect:/admin/operarios";
        } catch (IllegalArgumentException e) {
            if ("USERNAME_DUPLICADO".equals(e.getMessage())) {
                model.addAttribute("error", "⚠ Ya existe un operario con ese nombre de usuario.");
            } else {
                model.addAttribute("error", "❌ Error al guardar el operario.");
            }
            model.addAttribute("operario", usuario);
            return "operario-form";
        }
    }

    @GetMapping("/operarios/editar/{id}")
    public String editarOperario(@PathVariable Long id, Model model) {
        model.addAttribute("operario", usuarioService.buscarPorId(id));
        return "operario-form";
    }

    @GetMapping("/operarios/eliminar/{id}")
    public String eliminarOperario(@PathVariable Long id, RedirectAttributes flash) {
        try {
            usuarioService.eliminarUsuario(id);
            flash.addFlashAttribute("mensajeExito", "Operario eliminado exitosamente.");
        } catch (Exception e) {
            flash.addFlashAttribute("mensajeError", "No se puede eliminar: El operario tiene registros de asistencia o tareas.");
        }
        return "redirect:/admin/operarios";
    }

    @GetMapping("/lineas")
    public String listarLineas(Model model) {
        model.addAttribute("lineas", lineaProduccionService.listarLineas());
        return "linea-list";
    }

    @GetMapping("/lineas/nuevo")
    public String nuevaLinea(Model model) {
        model.addAttribute("linea", new LineaProduccion());
        return "linea-form";
    }

    @PostMapping("/lineas/guardar")
    public String guardarLinea(@ModelAttribute LineaProduccion linea, RedirectAttributes flash) {
        lineaProduccionService.crearLinea(linea);
        flash.addFlashAttribute("mensajeExito", "Línea de producción guardada.");
        return "redirect:/admin/lineas";
    }

    @GetMapping("/lineas/editar/{id}")
    public String editarLinea(@PathVariable Long id, Model model) {
        model.addAttribute("linea", lineaProduccionService.buscarPorId(id));
        return "linea-form";
    }

    @GetMapping("/lineas/eliminar/{id}")
    public String eliminarLinea(@PathVariable Long id, RedirectAttributes flash) {
        try {
            lineaProduccionService.eliminarLinea(id);
            flash.addFlashAttribute("mensajeExito", "Línea eliminada correctamente.");
        } catch (Exception e) {
            flash.addFlashAttribute("mensajeError", "No se puede eliminar: Hay operarios asignados a esta línea.");
        }
        return "redirect:/admin/lineas";
    }
}