package com.paradigmas.adventureworks.controller;

import com.paradigmas.adventureworks.service.ProveedorService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
public class ProveedorController {

    private final ProveedorService service;

    public ProveedorController(ProveedorService service) {
        this.service = service;
    }

    @GetMapping("/")
    public String inicio() {
        return "proveedores";
    }

    @PostMapping("/buscar")
    public String buscarProveedor(@RequestParam String proveedor, Model model) {
        try {
            Map<String, Object> resultado = service.generarReporte(proveedor);

            model.addAttribute("proveedorBuscado", proveedor);
            model.addAttribute("productos", resultado.get("productos"));
            model.addAttribute("precioPromedio", resultado.get("precioPromedio"));
            model.addAttribute("productoMayorTiempo", resultado.get("productoMayorTiempo"));

        } catch (Exception e) {
            model.addAttribute("error", "Error: " + e.getMessage());
        }

        return "proveedores";
    }
}
