package com.patitofeliz.main.model.conexion.proveedor;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Pedido 
{
    private int id;
    private int idProveedor;
    private int idSucursal;
    private String fechaPeticion;
}
