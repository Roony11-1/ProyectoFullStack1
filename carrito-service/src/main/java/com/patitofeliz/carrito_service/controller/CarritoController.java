package com.patitofeliz.carrito_service.controller;

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

import com.patitofeliz.carrito_service.model.Carrito;
import com.patitofeliz.carrito_service.model.conexion.Usuario;
import com.patitofeliz.carrito_service.service.CarritoService;

@RestController
@RequestMapping("/carrito")
public class CarritoController 
{
    @Autowired
    private CarritoService carritoService;

    @GetMapping
    public ResponseEntity<List<EntityModel<Carrito>>> getCarritos()
    {
        List<Carrito> carritos = carritoService.getCarritos();

        if (carritos.isEmpty())
            return ResponseEntity.noContent().build();

        List<EntityModel<Carrito>> carritosConLinks = new ArrayList<>();
        for (Carrito carrito : carritos) 
        {
            EntityModel<Carrito> recurso = EntityModel.of(carrito,
                linkTo(methodOn(CarritoController.class).getCarrito(carrito.getId())).withSelfRel()
            );
            carritosConLinks.add(recurso);
        }
        return ResponseEntity.ok(carritosConLinks);
    }

    @GetMapping("/usuario/{id}")
    public ResponseEntity<List<Carrito>> getCarritosUsuarioId(@PathVariable("id") int id)
    {
        List<Carrito> carritosId = carritoService.getCarritosByUsuarioid(id);

        if (carritosId.isEmpty())
            return ResponseEntity.noContent().build();

        return ResponseEntity.ok(carritosId);
    }

    @GetMapping("/sucursal/{id}")
    public ResponseEntity<List<Carrito>> getCarritosSucursalId(@PathVariable("id") int id)
    {
        List<Carrito> carritosId = carritoService.getCarritosBySucursalid(id);

        if (carritosId.isEmpty())
            return ResponseEntity.noContent().build();

        return ResponseEntity.ok(carritosId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Carrito>> getCarrito(@PathVariable("id") int id)
    {
        Carrito carrito = carritoService.getCarrito(id);

        if (carrito == null)
            return ResponseEntity.noContent().build();

        EntityModel<Carrito> recurso = EntityModel.of(carrito,
                linkTo(methodOn(CarritoController.class).getCarrito(carrito.getId())).withSelfRel(),
                linkTo(methodOn(CarritoController.class).getCarritos()).withRel("GET/carritos"),
                linkTo(methodOn(CarritoController.class).getCarritosUsuarioId(carrito.getUsuarioId())).withRel("GET/filtrarPorUsuario"),
                linkTo(methodOn(CarritoController.class).getCarritosUsuarioId(carrito.getSucursalId())).withRel("GET/filtrarPorSucursal"),
                linkTo(methodOn(CarritoController.class).actualizarCarrito(carrito.getId(), null)).withRel("PUT/actualizarCarrito")
        );

        return ResponseEntity.ok(recurso);
    }

    @GetMapping("/verificar/{id}")
    public ResponseEntity<Boolean> existeUsuarioPorId(@PathVariable int id) 
    {   
        boolean existe = carritoService.existePorId(id);
        return ResponseEntity.ok(existe);
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
    public ResponseEntity<Carrito> borrarCarrito(@PathVariable int id)
    {
        Carrito carrito = carritoService.getCarrito(id);

        if (carrito == null)
            return ResponseEntity.notFound().build();

        carritoService.borrar(id);
        return ResponseEntity.noContent().build();
    }

    // Metodos que me entregan los hateoas -- me chorie de ponerlos uno a uno xD como los weones ya basta
    private EntityModel<Carrito> hateoasSingular(Carrito carrito) 
    {
        int id = carrito.getId();

        return EntityModel.of(carrito,
            linkTo(methodOn(UsuarioController.class).obtenerUsuario(id)).withSelfRel(),
            linkTo(methodOn(UsuarioController.class).listarUsuarios()).withRel("GET/usuarios"),
            linkTo(methodOn(UsuarioController.class).getUsuarioReviews(id)).withRel("GET/reviews"),
            linkTo(methodOn(UsuarioController.class).getUsuarioCarritos(id)).withRel("GET/carritos"),
            linkTo(methodOn(UsuarioController.class).getUsuarioVentas(id)).withRel("GET/ventas"),
            linkTo(methodOn(UsuarioController.class).actualizarUsuario(id, null)).withRel("PUT/actualizarUsuario")
        );
    }

    private List<EntityModel<Carrito>> hateoasPlural(List<Usuario> carritos) 
    {
        List<EntityModel<Carrito>> listaconLinks = new ArrayList<>();
        
        for (Carrito carrito : carritos) 
        {
            EntityModel<Carrito> recurso = EntityModel.of(usuario,
                linkTo(methodOn(UsuarioController.class).obtenerUsuario(usuario.getId())).withSelfRel()
            );
            listaconLinks.add(recurso);
        }

        return listaconLinks;
    }
}
