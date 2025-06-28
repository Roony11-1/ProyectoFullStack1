package com.patitofeliz.supplier_service.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

    public ResponseEntity<List<Proveedor>> listarProveedores() 
    {
        return ResponseEntity.ok(proveedorService.getProveedores());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Proveedor> obtenerProveedor(@PathVariable int id) 
    {
        Proveedor proveedor = proveedorService.getProveedor(id);
        if (proveedor == null)
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(proveedor);
    }

    @PostMapping
    public ResponseEntity<Proveedor> crearProveedor(@RequestBody Proveedor proveedor) 
    {
        Proveedor nuevo = proveedorService.guardar(proveedor);
        return ResponseEntity.ok(nuevo);
    }

    @PostMapping("/lote")
    public ResponseEntity<List<Proveedor>> registrarProveedores(@RequestBody List<Proveedor> listaProveedores)
    {
        List<Proveedor> listaRegistros =  proveedorService.guardarLote(listaProveedores);

        return ResponseEntity.ok(listaRegistros);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Proveedor> actualizarProveedor(@PathVariable int id, @RequestBody Proveedor proveedorActualizado) 
    {
        Proveedor actualizado = proveedorService.actualizaProveedor(id, proveedorActualizado);
        
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProveedor(@PathVariable int id) 
    {
        proveedorService.borrar(id);
        return ResponseEntity.noContent().build();
    }
}
