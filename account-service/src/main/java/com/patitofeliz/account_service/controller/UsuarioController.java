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
import com.patitofeliz.account_service.service.UsuarioService;
import com.patitofeliz.main.model.conexion.carrito.Carrito;
import com.patitofeliz.main.model.conexion.review.Review;
import com.patitofeliz.main.model.conexion.venta.Venta;

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
        
        return ResponseEntity.ok(hateoasPlural(usuarios));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Usuario>> obtenerUsuario(@PathVariable("id") int  id)
    {
        Usuario usuario = usuarioService.getUsuario(id);

        if (usuario == null)
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(hateoasSingular(usuario));
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

        return ResponseEntity.ok(hateoasSingular(nuevo));
    }

    @PostMapping("/lote")
    public ResponseEntity<List<EntityModel<Usuario>>> registrarUsuarios(@RequestBody List<Usuario> listaUsuarios)
    {
        List<Usuario> listaRegistros =  usuarioService.registrarLote(listaUsuarios);

        return ResponseEntity.ok(hateoasPlural(listaRegistros));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<EntityModel<Usuario>> actualizarUsuario(@PathVariable("id") int id, @RequestBody Usuario usuario)
    {
        Usuario actualizado = usuarioService.actualizar(id, usuario);

        return ResponseEntity.ok(hateoasSingular(actualizado));
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

    // Metodos que me entregan los hateoas -- me chorie de ponerlos uno a uno xD como los weones ya basta
    private EntityModel<Usuario> hateoasSingular(Usuario usuario) 
    {
        int id = usuario.getId();

        return EntityModel.of(usuario,
            linkTo(methodOn(UsuarioController.class).obtenerUsuario(id)).withSelfRel(),
            linkTo(methodOn(UsuarioController.class).listarUsuarios()).withRel("GET/usuarios"),
            linkTo(methodOn(UsuarioController.class).getUsuarioReviews(id)).withRel("GET/reviews"),
            linkTo(methodOn(UsuarioController.class).getUsuarioCarritos(id)).withRel("GET/carritos"),
            linkTo(methodOn(UsuarioController.class).getUsuarioVentas(id)).withRel("GET/ventas"),
            linkTo(methodOn(UsuarioController.class).actualizarUsuario(id, null)).withRel("PUT/actualizarUsuario")
        );
    }

    private List<EntityModel<Usuario>> hateoasPlural(List<Usuario> usuarios) 
    {
        List<EntityModel<Usuario>> listaconLinks = new ArrayList<>();
        
        for (Usuario usuario : usuarios) 
        {
            EntityModel<Usuario> recurso = EntityModel.of(usuario,
                linkTo(methodOn(UsuarioController.class).obtenerUsuario(usuario.getId())).withSelfRel()
            );
            listaconLinks.add(recurso);
        }

        return listaconLinks;
    }
}
