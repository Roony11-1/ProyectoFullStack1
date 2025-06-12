package com.patitofeliz.admin_service.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
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
    public ResponseEntity<List<EntityModel<Alerta>>> listarAlertas()
    {
        List<Alerta> alertas = alertaService.getAlertas();

        if (alertas.isEmpty())
            return ResponseEntity.noContent().build();

        return ResponseEntity.ok(hateoasPlural(alertas));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Alerta>> obtenerAlerta(@PathVariable("id") int  id)
    {
        Alerta alerta = alertaService.getAlerta(id);

        if (alerta == null)
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(hateoasSingular(alerta));
    }

    @GetMapping("/filtrar/{tipoAlerta}")
    public ResponseEntity<List<EntityModel<Alerta>>> obtenerAlertaTipo(@PathVariable("tipoAlerta") String tipoAlerta)
    {
        List<Alerta> listaAlertasFiltrada = alertaService.getAlertaByTipoAlerta(tipoAlerta);

        if (listaAlertasFiltrada.isEmpty() || tipoAlerta.equalsIgnoreCase("aviso"))
            return ResponseEntity.noContent().build();
        
        return ResponseEntity.ok(hateoasPlural(listaAlertasFiltrada));
    }

    @PostMapping
    public ResponseEntity<EntityModel<Alerta>> registrarAlerta(@RequestBody Alerta alerta)
    {
        Alerta alertaNueva = alertaService.guardar(alerta);
        
        return ResponseEntity.ok(hateoasSingular(alertaNueva));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<EntityModel<Alerta>> actualizarAlerta(@PathVariable("id") int id, @RequestBody Alerta alerta)
    {
        Alerta actualizada = alertaService.actualizar(id, alerta);

        return ResponseEntity.ok(hateoasSingular(actualizada));
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

    // Metodos que me entregan los hateoas -- me chorie de ponerlos uno a uno xD como los weones ya basta
    private EntityModel<Alerta> hateoasSingular(Alerta alerta) 
    {
        int id = alerta.getId();

        String tipo = alerta.getTipoAlerta().substring("Aviso: ".length());

        return EntityModel.of(alerta,
            linkTo(methodOn(AlertaController.class).obtenerAlerta(id)).withSelfRel(),
            linkTo(methodOn(AlertaController.class).listarAlertas()).withRel("GET/alertas"),
            linkTo(methodOn(AlertaController.class).obtenerAlertaTipo(tipo)).withRel("GET/alertasTipo"),
            linkTo(methodOn(AlertaController.class).actualizarAlerta(id, null)).withRel("PUT/update")
        );
    }

    private List<EntityModel<Alerta>> hateoasPlural(List<Alerta> alertas) 
    {
        List<EntityModel<Alerta>> listaconLinks = new ArrayList<>();
        
        for (Alerta alerta : alertas) 
        {
            EntityModel<Alerta> recurso = EntityModel.of(alerta,
                linkTo(methodOn(AlertaController.class).obtenerAlerta(alerta.getId())).withSelfRel()
            );
            listaconLinks.add(recurso);
        }

        return listaconLinks;
    }
}
