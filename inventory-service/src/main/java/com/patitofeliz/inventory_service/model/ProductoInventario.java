package com.patitofeliz.inventory_service.model;

import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Embeddable // Significa que es una clase auxiliar
public class ProductoInventario 
{
    private int productoId;
    private String nombre;
    private String marca;
    private int precio;
    private int cantidad;
}
