package com.patitofeliz.main.model.dto.sucursal;

import com.patitofeliz.main.model.conexion.inventario.Inventario;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SucursalInventarioDTO 
{
    private int idSucursal;
    private Inventario inventario;
}


