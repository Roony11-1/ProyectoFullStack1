package com.patitofeliz.admin_service.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.patitofeliz.admin_service.model.Alerta;
import com.patitofeliz.admin_service.service.AlertaService;

@RestController
@RequestMapping("/alerta")
public class AlertaController 
{
    @Autowired
    private AlertaService alertaService;

    @GetMapping
    public ResponseEntity<List<Alerta>> listarAlertas()
    {
        List<Alerta> alertas = alertaService.getAlertas();

        if (alertas.isEmpty())
            return ResponseEntity.noContent().build();
        
        return ResponseEntity.ok(alertas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Alerta> obtenerAlerta(@PathVariable("id") int  id)
    {
        Alerta alerta = alertaService.getAlerta(id);

        if (alerta == null)
            return ResponseEntity.notFound().build();
        
        return ResponseEntity.ok(alerta);
    }

    @PostMapping
    public ResponseEntity<Alerta> registrarAlerta(@RequestBody Alerta alerta)
    {
        Alerta alertaNueva = alertaService.guardar(alerta);
        
        return ResponseEntity.ok(alertaNueva);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Alerta> actualizarAlerta(@PathVariable("id") int id, @RequestBody Alerta alerta)
    {
        Alerta actualizada = alertaService.actualizar(id, alerta);

        return ResponseEntity.ok(actualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Alerta> borrarAlerta(@PathVariable int id)
    {
        Alerta alerta = alertaService.getAlerta(id);

        if (alerta == null)
            return ResponseEntity.notFound().build();

        alertaService.borrar(id);
        
        return ResponseEntity.noContent().build();
    }
}
