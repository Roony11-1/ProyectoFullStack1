package com.patitofeliz.main.model.conexion.venta;

import java.util.List;

import com.patitofeliz.main.model.conexion.carrito.CarritoProducto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Venta 
{
    private int id;
    private int usuarioId;
    private int vendedorId;
    private int carritoId;
    
    private List<CarritoProducto> listaProductos;
    private int total;
}
