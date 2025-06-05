package com.patitofeliz.carrito_service.model;

import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Embeddable // Significa que es una clase auxiliar
public class CarritoProducto 
{
    private int productoId;
    private int cantidad;
}