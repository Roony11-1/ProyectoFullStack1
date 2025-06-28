package com.patitofeliz.main.model.dto.sucursal;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SucursalListaEnterosDTO 
{
    private int idSucursal;
    private List<Integer> listaEnteros;
}


