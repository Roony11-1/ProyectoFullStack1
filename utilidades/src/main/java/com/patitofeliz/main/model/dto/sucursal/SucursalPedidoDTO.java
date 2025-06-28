package com.patitofeliz.main.model.dto.sucursal;

import com.patitofeliz.main.model.conexion.proveedor.Pedido;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SucursalPedidoDTO 
{
    private int idSucursal;
    private Pedido pedido;
}


