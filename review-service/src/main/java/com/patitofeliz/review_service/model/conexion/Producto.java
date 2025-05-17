package com.patitofeliz.review_service.model.conexion;


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
