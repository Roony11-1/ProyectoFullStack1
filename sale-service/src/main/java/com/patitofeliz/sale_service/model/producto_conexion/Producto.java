package com.patitofeliz.sale_service.model.producto_conexion;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Producto 
{
    private int id;
    private String nombre;
    private String marca;
    private int precio;
    private int cantidad;
}
