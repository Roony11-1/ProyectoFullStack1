package com.patitofeliz.main.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProductoInventarioDTO 
{
    private int id;
    private int proveedorId;
    private String nombreProveedor;
    private String nombreProducto;
    private String marcaProducto;
    private int cantidad;
}
