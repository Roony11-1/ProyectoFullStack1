package com.patitofeliz.sucursal_service.controller;

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

import com.patitofeliz.sucursal_service.model.Sucursal;
import com.patitofeliz.sucursal_service.model.conexion.Inventario;
import com.patitofeliz.sucursal_service.model.conexion.ProductoInventario;
import com.patitofeliz.sucursal_service.model.conexion.Usuario;
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
    public ResponseEntity<Sucursal> obtenerSucursal(@PathVariable("id") int  id)
    {
        Sucursal sucursal = sucursalService.listarSucursal(id);

        if (sucursal == null)
            return ResponseEntity.notFound().build();
        
        return ResponseEntity.ok(sucursal);
    }

    @GetMapping("/inventario/{id}")
    public ResponseEntity<Inventario> obtenerInventarioSucursal(@PathVariable("id") int id)
    {
        Inventario inventario = sucursalService.listarInventarioSucursal(id);

        return ResponseEntity.ok(inventario);
    }

    @GetMapping("/empleados/{id}")
    public ResponseEntity<List<Integer>> obtenerEmpleadosSucursal(@PathVariable("id") int id)
    {
        List<Integer> empleados = sucursalService.listarEmpleadosSucursal(id);

        return ResponseEntity.ok(empleados);
    }

    @GetMapping("/verificar/{id}")
    public ResponseEntity<Boolean> existePorId(@PathVariable int id) 
    {   
        boolean existe = sucursalService.existePorId(id);
        return ResponseEntity.ok(existe);
    }

    @PostMapping
    public ResponseEntity<Sucursal> crearSucursal(@RequestBody Sucursal sucursal) 
    {
        Sucursal creada = sucursalService.guardar(sucursal);

        return ResponseEntity.ok(creada);
    }

    @PostMapping("/productos/{id}")
    public ResponseEntity<Sucursal> agregarProductosSucursal(@PathVariable("id") int id, @RequestBody List<ProductoInventario> productos)
    {
        Sucursal sucursal = sucursalService.añadirProductosSucursal(id, productos);

        return ResponseEntity.ok(sucursal);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Sucursal> borrarSucursal(@PathVariable int id)
    {
        Sucursal sucursal = sucursalService.listarSucursal(99);

        if (sucursal == null)
            return ResponseEntity.notFound().build();

    sucursalService.borrar(id);
        return ResponseEntity.noContent().build();
    }
}
