package com.patitofeliz.main.model.conexion.inventario;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Inventario 
{
    private int id;

    private List<ProductoInventario> listaProductos;
}
