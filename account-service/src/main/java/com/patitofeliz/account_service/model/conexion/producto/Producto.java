package com.patitofeliz.account_service.model.conexion.producto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Producto 
{
    private int id;
    private String nombre;
    private String marca;
    private int precio;
    private int cantidadInventario;
}