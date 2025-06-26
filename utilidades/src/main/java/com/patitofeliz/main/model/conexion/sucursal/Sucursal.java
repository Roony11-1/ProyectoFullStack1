package com.patitofeliz.main.model.conexion.sucursal;

import java.util.List;

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

    private List<Integer> listaEmpleados;
}
