package com.patitofeliz.account_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.patitofeliz.account_service.model.Usuario;
import com.patitofeliz.account_service.service.UsuarioService;

@RestController
@RequestMapping("/auth")
public class LoginController 
{
    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Usuario usuarioLogin)
    {
        Usuario usuario = usuarioService.findByEmail(usuarioLogin.getEmail());

        if (usuario == null)
            return ResponseEntity.notFound().build();

        if (!usuario.getPassword().equals(usuarioLogin.getPassword()))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Estado: Password incorrecta");
        
        return ResponseEntity.ok("Estado: Logeado");
    }
}
