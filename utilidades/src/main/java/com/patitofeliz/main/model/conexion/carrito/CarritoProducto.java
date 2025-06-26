package com.patitofeliz.main.model.conexion.carrito;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CarritoProducto 
{
    private int productoId;
    private int cantidad;
}