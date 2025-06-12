package com.patitofeliz.sale_service.model.conexion;

import java.util.List;

import com.patitofeliz.sale_service.model.CarritoProducto;

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
