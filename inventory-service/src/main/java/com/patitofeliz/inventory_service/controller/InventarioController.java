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

    @GetMapping
    public List<Inventario> getInventarios() 
    {
        return inventarioService.getInventarios();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Inventario> getInventario(@PathVariable int id) 
    {
        Inventario inventario = inventarioService.getInventario(id);
        if (inventario == null)
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(inventario);
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
