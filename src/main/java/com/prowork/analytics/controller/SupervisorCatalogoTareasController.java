package com.prowork.analytics.controller;

import com.prowork.analytics.model.Tarea;
import com.prowork.analytics.service.TareaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/supervisor/catalogo-tareas")
public class SupervisorCatalogoTareasController {

    @Autowired
    private TareaService tareaService;

    @GetMapping
    public String listarTareas(Model model) {
        model.addAttribute("tareas", tareaService.listarTareas());
        return "catalogo-tareas";
    }

    @GetMapping("/nuevo")
    public String nuevaTarea(Model model) {
        model.addAttribute("tarea", new Tarea());
        return "catalogo-tareas-form";
    }

    @GetMapping("/editar/{id}")
    public String editarTarea(@PathVariable Long id, Model model) {
        Tarea tarea = tareaService.buscarPorId(id);
        model.addAttribute("tarea", tarea);
        return "catalogo-tareas-form";
    }

    @PostMapping("/guardar")
    public String guardarTarea(@ModelAttribute Tarea tarea) {
        tareaService.guardar(tarea);
        return "redirect:/supervisor/catalogo-tareas";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarTarea(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            tareaService.eliminar(id);
            redirectAttributes.addFlashAttribute(
                    "mensajeExito",
                    "Tarea eliminada correctamente."
            );
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute(
                    "mensajeError",
                    e.getMessage()
            );
        }
        return "redirect:/supervisor/catalogo-tareas";
    }
}
