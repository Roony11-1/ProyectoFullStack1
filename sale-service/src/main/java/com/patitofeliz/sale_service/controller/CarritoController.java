package com.patitofeliz.sale_service.controller;

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

import com.patitofeliz.sale_service.model.Carrito;
import com.patitofeliz.sale_service.service.CarritoService;

@RestController
@RequestMapping("/carrito")
public class CarritoController 
{
    @Autowired
    private CarritoService carritoService;

    @GetMapping
    public ResponseEntity<List<Carrito>> getCarritos()
    {
        List<Carrito> carritos = carritoService.getCarritos();

        if (carritos.isEmpty())
            return ResponseEntity.noContent().build();

        return ResponseEntity.ok(carritos);
    }

    @GetMapping("/usuario/{id}")
    public ResponseEntity<List<Carrito>> getCarritosUsuarioId(@PathVariable("id") int id)
    {
        List<Carrito> carritosId = carritoService.getCarritosByUsuarioid(id);

        if (carritosId.isEmpty())
            return ResponseEntity.noContent().build();

        return ResponseEntity.ok(carritosId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Carrito> getCarrito(@PathVariable("id") int id)
    {
        Carrito carrito = carritoService.getCarrito(id);

        if (carrito == null)
            return ResponseEntity.noContent().build();

        return ResponseEntity.ok(carrito);
    }

    @PostMapping
    public ResponseEntity<Carrito> guardarCarrito(@RequestBody Carrito carrito)
    {
        Carrito nuevoCarrito = carritoService.guardar(carrito);

        return ResponseEntity.ok(nuevoCarrito);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Carrito> actualizarCarrito(@PathVariable("id") int id, @RequestBody Carrito carrito)
    {
        Carrito actualizado = carritoService.actualizar(id, carrito);

        return ResponseEntity.ok(actualizado);
    }

        @DeleteMapping("/{id}")
    public ResponseEntity<Carrito> borrarUsuario(@PathVariable int id)
    {
        Carrito carrito = carritoService.getCarrito(id);

        if (carrito == null)
            return ResponseEntity.notFound().build();

        carritoService.borrar(id);
        return ResponseEntity.noContent().build();
    }
}
