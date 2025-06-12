package com.patitofeliz.sucursal_service.model.conexion;

import java.util.List;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Venta 
{
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;

    private int usuarioId;
    private int vendedorId;
    private int carritoId;
    private int sucursalId;
    private List<CarritoProducto> listaProductos;
    private int total;
}
