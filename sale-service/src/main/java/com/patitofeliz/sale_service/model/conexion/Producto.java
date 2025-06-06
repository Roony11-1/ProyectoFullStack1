package com.patitofeliz.sale_service.model.conexion;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Producto 
{
    private String nombre;
    private String marca;
    private int precio;
}
