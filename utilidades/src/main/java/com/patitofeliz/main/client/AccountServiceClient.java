package com.patitofeliz.main.client;

import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.patitofeliz.main.model.conexion.usuario.Usuario;

@Service
public class AccountServiceClient 
{
    @Autowired
    private RestTemplate restTemplate;

    private static final String USUARIO_API = "http://localhost:8001/usuario";

    public Usuario getUsuario(int usuarioId) 
    {
        Usuario usuario = restTemplate.getForObject(USUARIO_API + "/" + usuarioId, Usuario.class);

        if (usuario == null)
            throw new NoSuchElementException("Usuario no encontrado con ID: " + usuarioId);

        return usuario;
    }
}
