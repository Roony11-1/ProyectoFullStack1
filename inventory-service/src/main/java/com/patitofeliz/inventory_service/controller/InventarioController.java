package com.patitofeliz.inventory_service.controller;

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

import com.patitofeliz.inventory_service.model.Inventario;
import com.patitofeliz.inventory_service.model.ProductoInventario;
import com.patitofeliz.inventory_service.service.InventarioService;

@RestController
@RequestMapping("/inventarios")
public class InventarioController 
{

    @Autowired
    private InventarioService inventarioService;

    // Obtener todos los inventarios
    @GetMapping
    public List<Inventario> getInventarios() 
    {
        return inventarioService.getInventarios();
    }

    // Obtener inventario por id
    @GetMapping("/{id}")
    public ResponseEntity<Inventario> getInventario(@PathVariable int id) 
    {
        Inventario inventario = inventarioService.getInventario(id);
        if (inventario == null)
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(inventario);
    }

    // Crear o actualizar inventario completo
    @PostMapping
    public Inventario guardarInventario(@RequestBody Inventario inventario) 
    {
        return inventarioService.guardarInventario(inventario);
    }

    // Eliminar inventario por id
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> borrarInventario(@PathVariable int id) 
    {
        inventarioService.borrarInventario(id);
        return ResponseEntity.noContent().build();
    }

    // Vaciar inventario
    @PostMapping("/{id}/vaciar")
    public Inventario vaciarInventario(@PathVariable int id) 
    {
        return inventarioService.vaciarInventario(id);
    }

    // Obtener productos de un inventario
    @GetMapping("/{id}/productos")
    public List<ProductoInventario> getProductosInventario(@PathVariable int id) 
    {
        return inventarioService.getProductosInventarioId(id);
    }

    // Agregar productos al inventario
    @PostMapping("/{id}/productos")
    public Inventario agregarProductosInventario(@PathVariable int id, @RequestBody List<ProductoInventario> productos) 
    {
        return inventarioService.agregarProductosInventario(id, productos);
    }

    // Eliminar productos del inventario
    @DeleteMapping("/{id}/productos")
    public Inventario eliminarProductosInventario(@PathVariable int id, @RequestBody List<ProductoInventario> productosEliminar)
    {
        return inventarioService.eliminarProductoInventario(id, productosEliminar);
    }

    // Actualizar productos en inventario (en lote)
    @PutMapping("/{id}/productos")
    public Inventario actualizarProductosInventario(@PathVariable int id, @RequestBody List<ProductoInventario> productosActualizados) 
    {
        return inventarioService.actualizarProductosEnInventario(id, productosActualizados);
    }
}
