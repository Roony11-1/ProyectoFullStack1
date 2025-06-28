package com.patitofeliz.supplier_service.model;

import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Embeddable // Significa que es una clase auxiliar
public class ProductoPedido 
{
    private int productoId;
    private int cantidad;
}
