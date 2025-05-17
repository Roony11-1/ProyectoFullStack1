package com.patitofeliz.account_service.model.conexion;

import java.util.List;

import jakarta.persistence.ElementCollection;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Carrito 
{
    private int id;
    private int usuarioId;

    @ElementCollection
    private List<CarritoProducto> listaProductos;
    private int total;
}
