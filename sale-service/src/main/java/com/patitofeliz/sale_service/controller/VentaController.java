package com.patitofeliz.sale_service.controller;

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
    public ResponseEntity<List<EntityModel<Venta>>> getVentas()
    {
        List<Venta> carritos = ventaService.getVentas();

        if (carritos.isEmpty())
            return ResponseEntity.noContent().build();

        return ResponseEntity.ok(hateoasPlural(carritos));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Venta>> getVenta(@PathVariable("id") int id)
    {
        Venta venta = ventaService.getVentaId(id);

        if (venta == null)
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(hateoasSingular(venta));
    }

    @GetMapping("/verificar/{id}")
    public ResponseEntity<Boolean> existePorId(@PathVariable int id) 
    {   
        boolean existe = ventaService.existePorId(id);
        return ResponseEntity.ok(existe);
    }

    @PostMapping
    public ResponseEntity<EntityModel<Venta>> ejecutarVenta(@RequestBody Venta venta)
    {
        Venta nuevaVenta = ventaService.generarVenta(venta);

        return ResponseEntity.ok(hateoasSingular(nuevaVenta));
    }

    @GetMapping("/usuario/{id}")
    public ResponseEntity<List<EntityModel<Venta>>> getVentasUsuarioId(@PathVariable("id") int id)
    {
        List<Venta> ventaId = ventaService.getVentasPorUsuarioId(id);

        if (ventaId.isEmpty())
            return ResponseEntity.noContent().build();

        return ResponseEntity.ok(hateoasPlural(ventaId));
    }

    @GetMapping("/vendedor/{id}")
    public ResponseEntity<List<EntityModel<Venta>>> getVentasVendedorId(@PathVariable("id") int id)
    {
        List<Venta> ventaId = ventaService.getVentasPorVendedorId(id);

        if (ventaId.isEmpty())
            return ResponseEntity.noContent().build();

        return ResponseEntity.ok(hateoasPlural(ventaId));
    }

    @GetMapping("/sucursal/{id}")
    public ResponseEntity<List<EntityModel<Venta>>> getVentasSucursalId(@PathVariable("id") int id)
    {
        List<Venta> ventaId = ventaService.getVentasPorSucursalId(id);

        if (ventaId.isEmpty())
            return ResponseEntity.noContent().build();

        return ResponseEntity.ok(hateoasPlural(ventaId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Venta> borrarVenta(@PathVariable int id)
    {
        Venta venta = ventaService.getVentaId(id);

        if (venta == null)
            return ResponseEntity.notFound().build();

        ventaService.borrar(id);
        return ResponseEntity.noContent().build();
    }

    // Metodos que me entregan los hateoas -- me chorie de ponerlos uno a uno xD como los weones ya basta
    private EntityModel<Venta> hateoasSingular(Venta venta) 
    {
        int id = venta.getId();

        return EntityModel.of(venta,
            linkTo(methodOn(VentaController.class).getVenta(id)).withSelfRel(),
            linkTo(methodOn(VentaController.class).getVentas()).withRel("GET/ventas"),
            linkTo(methodOn(VentaController.class).getVentasUsuarioId(venta.getUsuarioId())).withRel("GET/ventasUsuario"),
            linkTo(methodOn(VentaController.class).getVentasSucursalId(venta.getSucursalId())).withRel("GET/ventasSucursal"),
            linkTo(methodOn(VentaController.class).getVentasVendedorId(venta.getVendedorId())).withRel("GET/ventasVendedor")
        );
    }

    private List<EntityModel<Venta>> hateoasPlural(List<Venta> ventas) 
    {
        List<EntityModel<Venta>> listaconLinks = new ArrayList<>();

        for (Venta venta : ventas) {
            EntityModel<Venta> recurso = EntityModel.of(venta,
                linkTo(methodOn(VentaController.class).getVenta(venta.getId())).withSelfRel()
            );
            listaconLinks.add(recurso);
        }

        return listaconLinks;
    }
}
