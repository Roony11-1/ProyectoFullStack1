package com.patitofeliz.account_service.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.patitofeliz.account_service.model.Usuario;
import com.patitofeliz.account_service.repository.UsuarioRepository;

@Service
public class UsuarioService 
{
    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<Usuario> getUsuarios()
    {
        return usuarioRepository.findAll();
    }

    public Usuario getUsuario(int id)
    {
        return usuarioRepository.findById(id).orElse(null);
    }

    public Usuario findByEmail(String email) 
    {
        return usuarioRepository.findByEmail(email).orElse(null);
    }

    public Usuario registrar(Usuario usuario)
    {
        // Verificación a nivel de software para evitar que nos ingresen email repetidos o nulos
        if (findByEmail(usuario.getEmail()) != null)
            throw new IllegalArgumentException("Ya existe un usuario con ese email");

        Usuario nuevo = usuarioRepository.save(usuario);

        return nuevo;
    }

    public Usuario actualizar(int id, Usuario usuarioActualizado)
    {
        Usuario usuarioActual = usuarioRepository.findById(id).orElse(null);

        if (usuarioActual == null)
            throw new NoSuchElementException("Usuario no encontrado");

        // Como se puede ver optional nos permite verificar si es nulo [otro.ispresent = otro != null]
        // se podria generar un metodo para delegar la responsabilidad de verificar esta condicion ya que la repetimos
        Optional<Usuario> otro = usuarioRepository.findByEmail(usuarioActualizado.getEmail());
        if (otro.isPresent() && otro.get().getId() != id)
            throw new IllegalArgumentException("Ya existe un usuario con ese email");

        usuarioActual.setNombreUsuario(usuarioActualizado.getNombreUsuario());
        usuarioActual.setEmail(usuarioActualizado.getEmail());
        usuarioActual.setPassword(usuarioActualizado.getPassword());
        usuarioActual.setTipoUsuario(usuarioActualizado.getTipoUsuario());

        return usuarioRepository.save(usuarioActual);
    }

    public void borrar(int id)
    {
        usuarioRepository.deleteById(id);
    }
}
