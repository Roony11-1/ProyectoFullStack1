package com.patitofeliz.carrito_service.model.conexion;

import java.util.List;

import jakarta.persistence.ElementCollection;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Sucursal 
{
    private int id;
    private String nombreSucursal;

    private int gerenteId;
    private int inventarioId;

    @ElementCollection
    private List<Integer> listaEmpleados;
}
