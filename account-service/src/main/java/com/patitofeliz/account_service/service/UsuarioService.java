package com.patitofeliz.account_service.service;

import java.util.List;

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

    public Usuario registrar(Usuario usuario)
    {
        Usuario nuevo = usuarioRepository.save(usuario);

        if (usuarioRepository.findByEmail(usuario.getEmail()).isPresent())
            throw new IllegalArgumentException("Ya existe un usuario con ese email");

        return nuevo;
    }

    public void borrar(int id)
    {
        usuarioRepository.deleteById(id);
    }

    public Usuario findByEmail(String email) 
    {
        return usuarioRepository.findByEmail(email).orElse(null);
    }
}
