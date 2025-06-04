package com.patitofeliz.sucursal_service.model;

import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Embeddable // Significa que es una clase auxiliar
public class Inventario 
{
    private int productoId;
    private String nombre;
    private String marca;
    private String precio;
    private String cantidad;
}