package com.patitofeliz.main.model.dto.sucursal;

import com.patitofeliz.main.model.conexion.inventario.Inventario;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SucursalInventarioDTO 
{
    private int idSucursal;
    private Inventario inventario;
}


