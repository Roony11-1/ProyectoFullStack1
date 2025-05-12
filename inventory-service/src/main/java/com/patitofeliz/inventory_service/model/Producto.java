package com.patitofeliz.inventory_service.model;

import jakarta.persistence.Entity;

@Entity
public class Producto 
{
    private int id;
    private String nombre;
    private String marca;
    private int precio;
    private int cantidad;
}
