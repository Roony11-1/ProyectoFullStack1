package com.patitofeliz.inventory_service.controller;

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

import com.patitofeliz.inventory_service.model.Inventario;
import com.patitofeliz.inventory_service.model.ProductoInventario;
import com.patitofeliz.inventory_service.service.InventarioService;

@RestController
@RequestMapping("/inventarios")
public class InventarioController 
{

    @Autowired
    private InventarioService inventarioService;

    @GetMapping
    public ResponseEntity<List<EntityModel<Inventario>>> getInventarios() 
    {
        List<Inventario> lista = inventarioService.getInventarios();
        List<EntityModel<Inventario>> inventarioResources = new ArrayList<>();

        for (Inventario inventario : lista) {
            EntityModel<Inventario> recurso = EntityModel.of(inventario,
                linkTo(methodOn(InventarioController.class).getInventario(inventario.getId())).withSelfRel(),
                linkTo(methodOn(InventarioController.class).getProductosInventario(inventario.getId())).withRel("GET/productos"),
                linkTo(methodOn(InventarioController.class).vaciarInventario(inventario.getId())).withRel("POST/vaciar"),
                linkTo(methodOn(InventarioController.class).actualizarProductosInventario(inventario.getId(), null)).withRel("PUT/actualizar-productos"),
                linkTo(methodOn(InventarioController.class).eliminarProductosInventario(inventario.getId(), null)).withRel("DELETE/eliminar-productos")
            );
            inventarioResources.add(recurso);
        }

        return ResponseEntity.ok(inventarioResources);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Inventario>> getInventario(@PathVariable int id) 
    {
        Inventario inventario = inventarioService.getInventario(id);
        if (inventario == null)
            return ResponseEntity.notFound().build();
            
        EntityModel<Inventario> recurso = EntityModel.of(inventario,
        linkTo(methodOn(InventarioController.class).getInventarios()).withRel("GET/inventarios"),
        linkTo(methodOn(InventarioController.class).getProductosInventario(id)).withRel("GET/productos"),
        linkTo(methodOn(InventarioController.class).vaciarInventario(id)).withRel("POST/vaciar"),
        linkTo(methodOn(InventarioController.class).actualizarProductosInventario(id, null)).withRel("PUT/actualizar-productos"),
        linkTo(methodOn(InventarioController.class).eliminarProductosInventario(id, null)).withRel("DELETE/eliminar-productos")
       
        );


        return ResponseEntity.ok(recurso);
    }

    @GetMapping("/verificar/{id}")
    public ResponseEntity<Boolean> existePorId(@PathVariable int id) 
    {   
        boolean existe = inventarioService.existePorId(id);
        return ResponseEntity.ok(existe);
    }

    @PostMapping
    public ResponseEntity<Inventario> guardarInventario(@RequestBody Inventario inventario) 
    {
        return ResponseEntity.ok(inventarioService.guardarInventario(inventario));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> borrarInventario(@PathVariable int id) 
    {
        inventarioService.borrarInventario(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/vaciar")
    public ResponseEntity<Inventario> vaciarInventario(@PathVariable int id) 
    {
        if (!inventarioService.existePorId(id))
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(inventarioService.vaciarInventario(id));
    }

    @GetMapping("/{id}/productos")
    public List<ProductoInventario> getProductosInventario(@PathVariable int id) 
    {
        return inventarioService.getProductosInventarioId(id);
    }

    @PostMapping("/{id}/productos")
    public ResponseEntity<Inventario> agregarProductosInventario(@PathVariable int id, @RequestBody List<ProductoInventario> productos) 
    {
        return ResponseEntity.ok(inventarioService.agregarProductosInventario(id, productos));
    }

    @DeleteMapping("/{id}/productos")
    public ResponseEntity<Inventario> eliminarProductosInventario(@PathVariable int id, @RequestBody List<ProductoInventario> productosEliminar)
    {
        Inventario inventarioActualizado = inventarioService.eliminarProductoInventario(id, productosEliminar);
    
        return ResponseEntity.ok(inventarioActualizado);
    }

    @PutMapping("/{id}/productos")
    public ResponseEntity<Inventario> actualizarProductosInventario(@PathVariable int id, @RequestBody List<ProductoInventario> productosActualizados) 
    {
        Inventario inventarioActualizado = inventarioService.actualizarProductosEnInventario(id, productosActualizados);

        return ResponseEntity.ok(inventarioActualizado);
    }
}
