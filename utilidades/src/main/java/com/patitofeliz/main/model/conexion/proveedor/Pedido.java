package com.patitofeliz.main.model.conexion.proveedor;

import java.util.List;

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

    private List<ProductoPedido> listaProductos;

    private int estadoPedido;

    public Pedido(int idProveedor, int idSucursal, int estadoPedido, List<ProductoPedido> listaProductos)
    {
        this.idProveedor = idProveedor;
        this.idSucursal = idSucursal;
        this.estadoPedido = estadoPedido;
        this.listaProductos = listaProductos;
    }
}
