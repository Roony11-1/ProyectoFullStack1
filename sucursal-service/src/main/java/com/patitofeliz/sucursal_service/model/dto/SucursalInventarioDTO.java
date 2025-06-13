package com.patitofeliz.sucursal_service.model.dto;
import com.patitofeliz.sucursal_service.model.conexion.Inventario;


    

public class SucursalInventarioDTO {
    private int idSucursal;
    private int idInventario;
    private Inventario inventario;

    public SucursalInventarioDTO(int idSucursal, int idInventario, Inventario inventario) {
        this.idSucursal = idSucursal;
        this.idInventario = idInventario;
        this.inventario = inventario;
    }

    public int getIdSucursal() {
        return idSucursal;
    }

    public int getIdInventario() {
        return idInventario;
    }

    public Inventario getInventario() {
        return inventario;
    }
}


