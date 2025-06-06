package com.patitofeliz.sale_service.model.conexion;

import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Embeddable // Significa que es una clase auxiliar
public class ProductoInventario 
{
    private int productoId;
    private int cantidad;
}
