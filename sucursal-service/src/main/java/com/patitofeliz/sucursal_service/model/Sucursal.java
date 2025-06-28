package com.patitofeliz.sucursal_service.model;

import java.util.List;

import com.patitofeliz.main.model.conexion.proveedor.Pedido;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
public class Sucursal 
{
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;
    private String nombreSucursal;

    private int gerenteId;
    private int inventarioId;

    @ElementCollection
    private List<Integer> listaEmpleados;
    @ElementCollection
    private List<Integer> listaProveedores;
    @ElementCollection
    private List<Pedido> listaPedidos;
}
