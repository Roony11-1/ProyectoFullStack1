package com.patitofeliz.sale_service.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @GetMapping
    public ResponseEntity<List<Venta>> getVentas()
    {
        List<Venta> carritos = ventaService.getVentas();

        if (carritos.isEmpty())
            return ResponseEntity.noContent().build();

        return ResponseEntity.ok(carritos);
    }

    @GetMapping("/verificar/{id}")
    public ResponseEntity<Boolean> existePorId(@PathVariable int id) 
    {   
        boolean existe = ventaService.existePorId(id);
        return ResponseEntity.ok(existe);
    }

    @PostMapping
    public ResponseEntity<Venta> ejecutarVenta(@RequestBody Venta venta)
    {
        Venta nuevaVenta = ventaService.generarVenta(venta);

        return ResponseEntity.ok(nuevaVenta);
    }

    @GetMapping("/usuario/{id}")
    public ResponseEntity<List<Venta>> getVentasUsuarioId(@PathVariable("id") int id)
    {
        List<Venta> ventaId = ventaService.getVentasPorUsuarioId(id);

        if (ventaId.isEmpty())
            return ResponseEntity.noContent().build();

        return ResponseEntity.ok(ventaId);
    }

    @GetMapping("/vendedor/{id}")
    public ResponseEntity<List<Venta>> getVentasVendedorId(@PathVariable("id") int id)
    {
        List<Venta> ventaId = ventaService.getVentasPorVendedorId(id);

        if (ventaId.isEmpty())
            return ResponseEntity.noContent().build();

        return ResponseEntity.ok(ventaId);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Venta> borrarUsuario(@PathVariable int id)
    {
        Venta venta = ventaService.getVentaId(id);

        if (venta == null)
            return ResponseEntity.notFound().build();

        ventaService.elimiarPorId(id);
        return ResponseEntity.noContent().build();
    }
}
