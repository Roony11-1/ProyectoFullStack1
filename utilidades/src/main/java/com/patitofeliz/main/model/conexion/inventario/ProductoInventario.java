package com.patitofeliz.main.model.conexion.inventario;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductoInventario 
{
    private int productoId;
    private int cantidad;
}
