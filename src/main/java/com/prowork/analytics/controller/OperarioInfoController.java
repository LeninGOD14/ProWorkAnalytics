package com.prowork.analytics.controller;

import com.prowork.analytics.model.OperarioInfo;
import com.prowork.analytics.repository.OperarioInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/supervisor/operarios/info")
public class OperarioInfoController {

    @Autowired
    private OperarioInfoRepository operarioInfoRepository;

    @GetMapping
    public String listarOperarios(Model model) {
        model.addAttribute("operarios", operarioInfoRepository.findAll());
        return "operario_info_list";
    }

    @GetMapping("/nuevo")
    public String nuevoOperario(Model model) {
        model.addAttribute("operarioInfo", new OperarioInfo());
        return "operario_info_form";
    }

    @PostMapping("/nuevo")
    public String guardarOperario(@ModelAttribute("operarioInfo") OperarioInfo operarioInfo) {
        operarioInfoRepository.save(operarioInfo);
        return "redirect:/supervisor/operarios/info";
    }

    @GetMapping("/editar/{id}")
    public String editarOperario(@PathVariable("id") Long id, Model model) {
        OperarioInfo operarioInfo = operarioInfoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Operario no encontrado con id: " + id));
        model.addAttribute("operarioInfo", operarioInfo);
        return "operario_info_form";
    }

    @PostMapping("/editar/{id}")
    public String actualizarOperario(@PathVariable("id") Long id,
                                     @ModelAttribute("operarioInfo") OperarioInfo operarioInfo) {
        OperarioInfo operarioExistente = operarioInfoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Operario no encontrado con id: " + id));

        operarioExistente.setNombre(operarioInfo.getNombre());
        operarioExistente.setApellido(operarioInfo.getApellido());
        operarioExistente.setTelefono(operarioInfo.getTelefono());
        operarioExistente.setCorreo(operarioInfo.getCorreo());
        operarioExistente.setTurno(operarioInfo.getTurno());
        operarioExistente.setEstado(operarioInfo.getEstado());
        operarioExistente.setFechaIngreso(operarioInfo.getFechaIngreso());

        operarioInfoRepository.save(operarioExistente);
        return "redirect:/supervisor/operarios/info";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarOperario(@PathVariable("id") Long id) {
        OperarioInfo operarioInfo = operarioInfoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Operario no encontrado con id: " + id));
        operarioInfoRepository.delete(operarioInfo);
        return "redirect:/supervisor/operarios/info";
    }
}
