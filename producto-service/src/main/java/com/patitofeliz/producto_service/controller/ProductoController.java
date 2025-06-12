package com.patitofeliz.producto_service.controller;

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

import com.patitofeliz.producto_service.model.Producto;
import com.patitofeliz.producto_service.model.conexion.Review;
import com.patitofeliz.producto_service.service.ProductoService;


@RestController
@RequestMapping("/producto")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @GetMapping
    public ResponseEntity<List<EntityModel<Producto>>> listarProductos(){
        List<Producto>productos = productoService.getProductos();
        if(productos.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(hateoasPlural(productos));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Producto>> obtenerProducto(@PathVariable("id") int id){
        Producto producto = productoService.getProducto(id);
        if(producto==null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(hateoasSingular(producto));
    }

    @GetMapping("/verificar/{id}")
    public ResponseEntity<Boolean> existePorId(@PathVariable int id) 
    {   
        boolean existe = productoService.existePorId(id);
        return ResponseEntity.ok(existe);
    }

    @PostMapping
    public ResponseEntity<Producto> guardarProducto(@RequestBody Producto producto)
    {
        Producto nuevProducto = productoService.registrar(producto);
        return ResponseEntity.ok(nuevProducto);
    }

    @PostMapping("/lote")
    public ResponseEntity<List<EntityModel<Producto>>> guardarLoteProductos(@RequestBody List<Producto> productos) 
    {
        List<Producto> productosRegistrados = productoService.registrarLote(productos);

        return ResponseEntity.ok(hateoasPlural(productosRegistrados));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Producto> actualizarProducto(@PathVariable("id") int id, @RequestBody Producto producto)
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
    
    // Metodos que me entregan los hateoas -- me chorie de ponerlos uno a uno xD como los weones ya basta
    private EntityModel<Producto> hateoasSingular(Producto producto) 
    {
        int id = producto.getId();

        return EntityModel.of(producto,
            linkTo(methodOn(ProductoController.class).obtenerProducto(id)).withSelfRel(),
            linkTo(methodOn(ProductoController.class).listarProductos()).withRel("GET/productos"),
            linkTo(methodOn(ProductoController.class).guardarLoteProductos(null)).withRel("POST/guardarLote"),
            linkTo(methodOn(ProductoController.class).getProductoReviews(id)).withRel("GET/reviews"),
            linkTo(methodOn(ProductoController.class).actualizarProducto(id, null)).withRel("PUT/actualizar")
        );
    }

    private List<EntityModel<Producto>> hateoasPlural(List<Producto> productos) 
    {
        List<EntityModel<Producto>> productosConLinks = new ArrayList<>();
        
        for (Producto producto : productos) 
        {
            EntityModel<Producto> recurso = EntityModel.of(producto,
                linkTo(methodOn(ProductoController.class).obtenerProducto(producto.getId())).withSelfRel(),
                linkTo(methodOn(ProductoController.class).listarProductos()).withRel("GET/productos")
            );
            productosConLinks.add(recurso);
        }

        return productosConLinks;
    }
}
