package com.patitofeliz.sale_service.model.conexion;

import java.util.List;

import jakarta.persistence.ElementCollection;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Inventario 
{
    private int id;

    @ElementCollection
    private List<ProductoInventario> listaProductos;
}
