package com.patitofeliz.account_service.controller;

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

import com.patitofeliz.account_service.model.Usuario;
import com.patitofeliz.account_service.model.conexion.Review;
import com.patitofeliz.account_service.service.UsuarioService;

@RestController
@RequestMapping("/usuario")
public class UsuarioController 
{
    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public ResponseEntity<List<Usuario>> listarUsuarios()
    {
        List<Usuario> usuarios = usuarioService.getUsuarios();

        if (usuarios.isEmpty())
            return ResponseEntity.noContent().build();
        
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> obtenerUsuario(@PathVariable("id") int  id)
    {
        Usuario usuario = usuarioService.getUsuario(id);

        if (usuario == null)
            return ResponseEntity.notFound().build();
        
        return ResponseEntity.ok(usuario);
    }

    @PostMapping
    public ResponseEntity<Usuario> registrarUsuario(@RequestBody Usuario usuario)
    {
        Usuario usuarioNuevo = usuarioService.registrar(usuario);
        
        return ResponseEntity.ok(usuarioNuevo);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Usuario> actualizarUsuario(@PathVariable("id") int id, @RequestBody Usuario usuario)
    {
        Usuario actualizado = usuarioService.actualizar(id, usuario);

        return ResponseEntity.ok(actualizado);
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
}
