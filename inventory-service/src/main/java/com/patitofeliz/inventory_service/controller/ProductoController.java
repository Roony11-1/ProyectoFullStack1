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

import com.patitofeliz.inventory_service.model.Producto;
import com.patitofeliz.inventory_service.model.conexion.Review;
import com.patitofeliz.inventory_service.service.ProductoService;


@RestController
@RequestMapping("/producto")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @GetMapping
    public ResponseEntity<List<Producto>> listarProductos(){
        List<Producto>productos = productoService.getProductos();
        if(productos.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(productos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Producto> obtenerProducto(@PathVariable("id") int id){
        Producto producto = productoService.getProducto(id);
        if(producto==null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(producto);    
    }

    @PostMapping
    public ResponseEntity<Producto> guardarProducto(@RequestBody Producto producto)
    {
        Producto nuevProducto = productoService.registrar(producto);
        return ResponseEntity.ok(nuevProducto);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Producto> actualizarUsuario(@PathVariable("id") int id, @RequestBody Producto producto)
    {
        Producto actualizado = productoService.actualizar(id, producto);

        return ResponseEntity.ok(actualizado);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Producto> borrarProducto(@PathVariable int id){
        Producto producto = productoService.getProducto(id);
        if(producto==null){
            return ResponseEntity.notFound().build();
        }
        productoService.borrar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/review/{id}")
    public ResponseEntity<List<Review>> getProductoReviews(@PathVariable int id) 
    {
        List<Review> listaReseñas = productoService.getReviewsByProductoId(id);

        return ResponseEntity.ok(listaReseñas);
    }
    
}
