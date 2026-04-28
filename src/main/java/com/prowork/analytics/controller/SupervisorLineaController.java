package com.prowork.analytics.controller;

import com.prowork.analytics.model.LineaProduccion;
import com.prowork.analytics.service.LineaProduccionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/supervisor/lineas-supervisor")
public class SupervisorLineaController {

    @Autowired
    private LineaProduccionService lineaService;

    @GetMapping
    public String listarLineas(Model model) {
        model.addAttribute("lineas", lineaService.listarLineas());
        return "linea-list";
    }

    @GetMapping("/nuevo")
    public String nuevaLinea(Model model) {
        model.addAttribute("linea", new LineaProduccion());
        return "linea-form";
    }

    @PostMapping("/guardar")
    public String guardarLinea(@ModelAttribute LineaProduccion linea) {
        lineaService.crearLinea(linea);
        return "redirect:/supervisor/lineas-supervisor";
    }

    @GetMapping("/editar/{id}")
    public String editarLinea(@PathVariable Long id, Model model) {
        LineaProduccion linea = lineaService.buscarPorId(id);
        if (linea != null) {
            model.addAttribute("linea", linea);
            return "linea-form";
        }
        return "redirect:/supervisor/lineas-supervisor";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarLinea(@PathVariable Long id,
            RedirectAttributes redirectAttributes) {

        try {
            lineaService.eliminarLinea(id);
            redirectAttributes.addFlashAttribute("mensajeExito",
                    "Línea eliminada correctamente.");

        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("mensajeError",
                    "No se puede eliminar la línea porque está asociada a una tarea.");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensajeError",
                    "Ocurrió un error al intentar eliminar la línea.");
        }

        return "redirect:/supervisor/lineas-supervisor";
    }
}
