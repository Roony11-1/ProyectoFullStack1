package com.patitofeliz.main.model.conexion.carrito;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarritoProducto 
{
    private int productoId;
    private int cantidad;
}