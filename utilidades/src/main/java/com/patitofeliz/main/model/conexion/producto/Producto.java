package com.patitofeliz.main.model.conexion.producto;

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
    private int idProveedor;
    private int precio;
}