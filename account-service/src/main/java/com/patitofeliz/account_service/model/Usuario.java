package com.patitofeliz.account_service.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data // Genera metodos básicos
@NoArgsConstructor // Constructor sin parametros
public class Usuario 
{
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;
    private String email;
    private String nombreUsuario;
    private String password;
    private String tipoUsuario;
}
