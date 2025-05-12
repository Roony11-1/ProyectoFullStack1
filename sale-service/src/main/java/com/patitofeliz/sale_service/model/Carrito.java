package com.patitofeliz.sale_service.model;

//import java.util.List;

//import com.patitofeliz.sale_service.model.producto_conexion.Producto;
//import com.patitofeliz.sale_service.model.facturacion.Facturacion;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Carrito 
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int usuarioId;
    //private List<Producto> listaProductos; // Agregar producto cuando se haga la conexion
    //private Facturacion Facturacion; // Crea boletas
}
