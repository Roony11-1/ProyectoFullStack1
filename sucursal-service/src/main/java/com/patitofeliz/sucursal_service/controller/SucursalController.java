package com.patitofeliz.sucursal_service.controller;

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

import com.patitofeliz.sucursal_service.model.Sucursal;
import com.patitofeliz.sucursal_service.model.conexion.Carrito;
import com.patitofeliz.sucursal_service.model.conexion.Inventario;
import com.patitofeliz.sucursal_service.model.conexion.ProductoInventario;
import com.patitofeliz.sucursal_service.model.conexion.Venta;
import com.patitofeliz.sucursal_service.service.SucursalService;

@RestController
@RequestMapping("/sucursal")
public class SucursalController 
{
    @Autowired
    private SucursalService sucursalService;

    @GetMapping
    public ResponseEntity<List<EntityModel<Sucursal>>> listarSucursales() 
    {
        List<Sucursal> sucursales = sucursalService.listarSucursales();

        if (sucursales.isEmpty())
            return ResponseEntity.noContent().build();

        List<EntityModel<Sucursal>> sucursalesConLinks = new ArrayList<>();
        for (Sucursal sucursal : sucursales) 
        {
            EntityModel<Sucursal> recurso = EntityModel.of(sucursal,
                linkTo(methodOn(SucursalController.class).obtenerSucursal(sucursal.getId())).withSelfRel()
            );
            sucursalesConLinks.add(recurso);
        }
        return ResponseEntity.ok(sucursalesConLinks);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Sucursal>> obtenerSucursal(@PathVariable("id") int  id)
    {
        Sucursal sucursal = sucursalService.listarSucursal(id);

        if (sucursal == null)
            return ResponseEntity.notFound().build();

        EntityModel<Sucursal> recurso = EntityModel.of(sucursal,
            linkTo(methodOn(SucursalController.class).listarSucursales()).withRel("sucursales"),
            linkTo(methodOn(SucursalController.class).obtenerInventarioSucursal(id)).withRel("GET/obtener inventario sucursal"),
            linkTo(methodOn(SucursalController.class).obtenerEmpleadosSucursal(id)).withRel("GET/obtener empleados sucursal"),
            linkTo(methodOn(SucursalController.class).obtenerVentasSucursal(id)).withRel("GET/obtener ventas sucursal"),
            linkTo(methodOn(SucursalController.class).obtenerCarritosSucursal(id)).withRel("GET/obtener carritos sucursal"),
            linkTo(methodOn(SucursalController.class).agregarProductosSucursal(id, new ArrayList<>())).withRel("POST/gregar producto inv. sucursal")
        );

        return ResponseEntity.ok(recurso);
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

    @GetMapping("/ventas/{id}")
    public ResponseEntity<List<Venta>> obtenerVentasSucursal(@PathVariable("id") int id)
    {
        List<Venta> ventas = sucursalService.listarVentasSucursal(id);

        return ResponseEntity.ok(ventas);
    }

    @GetMapping("/carrito/{id}")
    public ResponseEntity<List<Carrito>> obtenerCarritosSucursal(@PathVariable("id") int id)
    {
        List<Carrito> carritos = sucursalService.listarCarritosSucursal(id);

        return ResponseEntity.ok(carritos);
    }

    @GetMapping("/verificar/{id}")
    public ResponseEntity<Boolean> existePorId(@PathVariable int id) 
    {   
        boolean existe = sucursalService.existePorId(id);
        return ResponseEntity.ok(existe);
    }

    @PostMapping
    public ResponseEntity<EntityModel<Sucursal>> crearSucursal(@RequestBody Sucursal sucursal) 
    {
        Sucursal creada = sucursalService.guardar(sucursal);

        EntityModel<Sucursal> recurso = EntityModel.of(creada,
                linkTo(methodOn(SucursalController.class).obtenerSucursal(creada.getId())).withSelfRel()
        );

        return ResponseEntity.ok(recurso);
    }

    @PostMapping("/productos/{id}")
    public ResponseEntity<Inventario> agregarProductosSucursal(@PathVariable("id") int id, @RequestBody List<ProductoInventario> productos)
    {
        Inventario inventario = sucursalService.añadirProductosSucursal(id, productos);

        return ResponseEntity.ok(inventario);
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
