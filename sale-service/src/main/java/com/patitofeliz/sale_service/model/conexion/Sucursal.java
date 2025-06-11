package com.patitofeliz.sale_service.model.conexion;

import java.util.List;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Sucursal 
{
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;
    private String nombreSucursal;

    private int GerenteId;
    private int inventarioId;

    @ElementCollection
    private List<Integer> listaEmpleados;
}
