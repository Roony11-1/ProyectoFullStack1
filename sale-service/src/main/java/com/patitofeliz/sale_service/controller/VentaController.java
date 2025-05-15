package com.patitofeliz.sale_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.patitofeliz.sale_service.model.Venta;
import com.patitofeliz.sale_service.service.VentaService;

@RestController
@RequestMapping("/venta")
public class VentaController 
{
    @Autowired
    private VentaService ventaService;

    @PostMapping
    public ResponseEntity<Venta> ejecutarVenta(@RequestBody Venta venta)
    {
        Venta nuevaVenta = ventaService.generarVenta(venta);

        return ResponseEntity.ok(nuevaVenta);
    }
}
