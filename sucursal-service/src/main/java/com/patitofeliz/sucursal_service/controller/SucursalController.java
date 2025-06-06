package com.patitofeliz.sucursal_service.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.patitofeliz.sucursal_service.model.Sucursal;
import com.patitofeliz.sucursal_service.service.SucursalService;

@RestController
@RequestMapping("/sucursal")
public class SucursalController 
{
    @Autowired
    private SucursalService sucursalService;

    @GetMapping
    public ResponseEntity<List<Sucursal>> listarSucursales() 
    {
        List<Sucursal> sucursales = sucursalService.listarSucursales();

        if (sucursales.isEmpty())
            return ResponseEntity.noContent().build();

        return ResponseEntity.ok(sucursales);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Sucursal> obtenerUsuario(@PathVariable("id") int  id)
    {
        Sucursal sucursal = sucursalService.listarSucursal(id);

        if (sucursal == null)
            return ResponseEntity.notFound().build();
        
        return ResponseEntity.ok(sucursal);
    }

    @PostMapping
    public ResponseEntity<Sucursal> crearSucursal(@RequestBody Sucursal sucursal) 
    {
        Sucursal creada = sucursalService.guardar(sucursal);

        return ResponseEntity.ok(creada);
    }
}
