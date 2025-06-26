package com.patitofeliz.main.model.conexion.carrito;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Carrito 
{
    private int id;
    private int usuarioId;
    private int sucursalId;

    private List<CarritoProducto> listaProductos;
    private int total;
}
