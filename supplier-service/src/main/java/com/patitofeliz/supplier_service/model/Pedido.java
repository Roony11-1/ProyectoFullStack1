package com.patitofeliz.supplier_service.model;

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
public class Pedido 
{
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;
    private int idProveedor;
    private int idSucursal;
    private String fechaPeticion;

    @ElementCollection
    private List<ProductoPedido> listaProductos;

    private int estadoPedido;
}
