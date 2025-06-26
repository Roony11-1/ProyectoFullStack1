package com.patitofeliz.account_service.model.conexion.venta;

import java.util.List;

import com.patitofeliz.account_service.model.conexion.carrito.CarritoProducto;

import jakarta.persistence.ElementCollection;
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
    
    @ElementCollection
    private List<CarritoProducto> listaProductos;
    private int total;
}
