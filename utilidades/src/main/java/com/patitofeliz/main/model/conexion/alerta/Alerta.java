package com.patitofeliz.main.model.conexion.alerta;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Alerta 
{
    private int id;
    private String mensaje;
    private String tipoAlerta;
    private String fecha;

    public Alerta(String mensaje, String tipoAlerta)
    {
        this.mensaje = mensaje;
        this.tipoAlerta = tipoAlerta;
    }
}
