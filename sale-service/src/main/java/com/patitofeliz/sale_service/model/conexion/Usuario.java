package com.patitofeliz.sale_service.model.conexion;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Usuario 
{
    private String email;
    private String nombreUsuario;
    private String password;
    private String tipoUsuario;
}
