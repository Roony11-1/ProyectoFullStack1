package com.patitofeliz.account_service.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.patitofeliz.account_service.model.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer>
{
    Optional<Usuario> findByEmail(String email);
}
