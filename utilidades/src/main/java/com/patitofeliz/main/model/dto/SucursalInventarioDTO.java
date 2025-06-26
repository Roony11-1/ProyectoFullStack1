package com.patitofeliz.main.model.dto;

import com.patitofeliz.main.model.conexion.inventario.Inventario;

public class SucursalInventarioDTO {
    private int idSucursal;
    private Inventario inventario;

    public SucursalInventarioDTO(int idSucursal,Inventario inventario) {
        this.idSucursal = idSucursal;
        this.inventario = inventario;
    }

    public int getIdSucursal() {
        return idSucursal;
    }

    public Inventario getInventario() {
        return inventario;
    }
}


