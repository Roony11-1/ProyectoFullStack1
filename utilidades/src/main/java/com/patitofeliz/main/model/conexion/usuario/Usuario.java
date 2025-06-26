package com.patitofeliz.main.model.conexion.usuario;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Usuario 
{
    private int id;
    private String email;
    private String nombreUsuario;
    private String password;
    private String tipoUsuario;
}
