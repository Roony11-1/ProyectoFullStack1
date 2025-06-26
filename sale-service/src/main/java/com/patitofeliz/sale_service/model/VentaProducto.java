package com.patitofeliz.sale_service.model;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable // Significa que es una clase auxiliar
public class VentaProducto 
{
    private int productoId;
    private int cantidad;
}