package com.patitofeliz.sucursal_service.model.conexion;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable // Significa que es una clase auxiliar
public class ProductoInventario 
{
    private int productoId;
    private int cantidad;
}
