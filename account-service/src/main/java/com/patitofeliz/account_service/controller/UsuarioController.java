package com.patitofeliz.account_service.controller;

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

import com.patitofeliz.account_service.model.Usuario;
import com.patitofeliz.account_service.model.conexion.Carrito;
import com.patitofeliz.account_service.model.conexion.Review;
import com.patitofeliz.account_service.model.conexion.Venta;
import com.patitofeliz.account_service.service.UsuarioService;

@RestController
@RequestMapping("/usuario")
public class UsuarioController 
{
    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public ResponseEntity<List<EntityModel<Usuario>>> listarUsuarios()
    {
        List<Usuario> usuarios = usuarioService.getUsuarios();

        if (usuarios.isEmpty())
            return ResponseEntity.noContent().build();
        
        List<EntityModel<Usuario>> usuariosConLinks = new ArrayList<>();
        for (Usuario usuario : usuarios) 
        {
            EntityModel<Usuario> recurso = EntityModel.of(usuario,
                linkTo(methodOn(UsuarioController.class).obtenerUsuario(usuario.getId())).withSelfRel()
            );
            usuariosConLinks.add(recurso);
        }
        return ResponseEntity.ok(usuariosConLinks);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Usuario>> obtenerUsuario(@PathVariable("id") int  id)
    {
        Usuario usuario = usuarioService.getUsuario(id);

        if (usuario == null)
            return ResponseEntity.notFound().build();

        EntityModel<Usuario> recurso = EntityModel.of(usuario,
            linkTo(methodOn(UsuarioController.class).obtenerUsuario(id)).withSelfRel(),
            linkTo(methodOn(UsuarioController.class).listarUsuarios()).withRel("GET/usuarios"),
            linkTo(methodOn(UsuarioController.class).getUsuarioReviews(id)).withRel("GET/reviews"),
            linkTo(methodOn(UsuarioController.class).getUsuarioCarritos(id)).withRel("GET/carritos"),
            linkTo(methodOn(UsuarioController.class).getUsuarioVentas(id)).withRel("GET/ventas")
        );

        return ResponseEntity.ok(recurso);
    }

    @GetMapping("/verificar/{id}")
    public ResponseEntity<Boolean> existePorId(@PathVariable int id) 
    {   
        boolean existe = usuarioService.existePorId(id);
        return ResponseEntity.ok(existe);
    }

    @PostMapping
    public ResponseEntity<EntityModel<Usuario>> registrarUsuario(@RequestBody Usuario usuario)
    {
        Usuario nuevo = usuarioService.registrar(usuario);

        EntityModel<Usuario> recurso = EntityModel.of(nuevo,
                linkTo(methodOn(UsuarioController.class).obtenerUsuario(nuevo.getId())).withSelfRel(),
                linkTo(methodOn(UsuarioController.class).listarUsuarios()).withRel("GET/usuarios")
        );

        return ResponseEntity.ok(recurso);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<EntityModel<Usuario>> actualizarUsuario(@PathVariable("id") int id, @RequestBody Usuario usuario)
    {
        Usuario actualizado = usuarioService.actualizar(id, usuario);

        EntityModel<Usuario> recurso = EntityModel.of(actualizado,
                linkTo(methodOn(UsuarioController.class).obtenerUsuario(actualizado.getId())).withSelfRel()
        );

        return ResponseEntity.ok(recurso);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Usuario> borrarUsuario(@PathVariable int id)
    {
        Usuario usuario = usuarioService.getUsuario(id);

        if (usuario == null)
            return ResponseEntity.notFound().build();

        usuarioService.borrar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/review/{id}")
    public ResponseEntity<List<Review>> getUsuarioReviews(@PathVariable int id) 
    {
        List<Review> listaReseñas = usuarioService.getReviewsByUsuarioId(id);

        return ResponseEntity.ok(listaReseñas);
    }

    @GetMapping("/carrito/{id}")
    public ResponseEntity<List<Carrito>> getUsuarioCarritos(@PathVariable int id) 
    {
        List<Carrito> listaProducto = usuarioService.getCarritoByUsuarioId(id);

        return ResponseEntity.ok(listaProducto);
    }

    @GetMapping("/venta/{id}")
    public ResponseEntity<List<Venta>> getUsuarioVentas(@PathVariable int id) 
    {
        List<Venta> listaVentas = usuarioService.getVentasByUsuarioId(id);

        return ResponseEntity.ok(listaVentas);
    }
}
