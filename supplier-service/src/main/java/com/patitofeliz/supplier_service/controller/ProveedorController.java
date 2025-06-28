package com.patitofeliz.supplier_service.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.patitofeliz.supplier_service.model.Proveedor;
import com.patitofeliz.supplier_service.service.ProveedorService;

@RestController
@RequestMapping("/proveedor")
public class ProveedorController 
{

    @Autowired
    private ProveedorService proveedorService;

    @GetMapping

    public ResponseEntity<List<EntityModel<Proveedor>>> listarProveedores() 
    {
        return ResponseEntity.ok(hateoasPlural(proveedorService.getProveedores()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Proveedor>> obtenerProveedor(@PathVariable int id) 
    {
        Proveedor proveedor = proveedorService.getProveedor(id);
        if (proveedor == null)
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(hateoasSingular(proveedor));
    }

    @PostMapping
    public ResponseEntity<EntityModel<Proveedor>> crearProveedor(@RequestBody Proveedor proveedor) 
    {
        Proveedor nuevo = proveedorService.guardar(proveedor);
        return ResponseEntity.ok(hateoasSingular(nuevo));
    }

    @PostMapping("/lote")
    public ResponseEntity<List<EntityModel<Proveedor>>> registrarProveedores(@RequestBody List<Proveedor> listaProveedores)
    {
        List<Proveedor> listaRegistros =  proveedorService.guardarLote(listaProveedores);

        return ResponseEntity.ok(hateoasPlural(listaRegistros));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Proveedor>> actualizarProveedor(@PathVariable int id, @RequestBody Proveedor proveedorActualizado) 
    {
        Proveedor actualizado = proveedorService.actualizaProveedor(id, proveedorActualizado);
        
        return ResponseEntity.ok(hateoasSingular(actualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProveedor(@PathVariable int id) 
    {
        proveedorService.borrar(id);
        return ResponseEntity.noContent().build();
    }

    // Metodos que me entregan los hateoas -- me chorie de ponerlos uno a uno xD como los weones ya basta
    private EntityModel<Proveedor> hateoasSingular(Proveedor proveedor) 
    {
        int id = proveedor.getId();

        return EntityModel.of(proveedor,
            linkTo(methodOn(ProveedorController.class).listarProveedores()).withRel("pedidos")
        );
    }

    private List<EntityModel<Proveedor>> hateoasPlural(List<Proveedor> pedidos) 
    {
        List<EntityModel<Proveedor>> listaconLinks = new ArrayList<>();
        
        for (Proveedor pedido : pedidos) 
        {
            EntityModel<Proveedor> recurso = EntityModel.of(pedido,
                linkTo(methodOn(ProveedorController.class).listarProveedores()).withSelfRel()
            );
            listaconLinks.add(recurso);
        }

        return listaconLinks;
    }
}
