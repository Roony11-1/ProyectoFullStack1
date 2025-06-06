package com.patitofeliz.sale_service.model;

import java.util.List;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
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
    @ElementCollection
    private List<CarritoProducto> listaProductos;
    private int total;
}
