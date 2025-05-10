package com.patitofeliz.account_service.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.patitofeliz.account_service.model.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer>
{
    // Metodo abstracto con return de Optional, es una clase que es más comodo verificar y tratar condiciones
    // puedes preguntar si existe, y el caso que fuera nulo se maneja automaticamente
    // Ademas Jpa al seguir la estructura findBy... genera el metodo automaticamente no hay que crearlo nosotros
    // almenos se que es asi al buscar atributos (nombre, edad, correo)
    Optional<Usuario> findByEmail(String email);
}
