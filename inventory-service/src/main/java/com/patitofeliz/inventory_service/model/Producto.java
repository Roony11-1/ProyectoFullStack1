package com.patitofeliz.inventory_service.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.NoArgsConstructor;


@Entity
// Etiqueta de Jpa
// Etiquetas de lombook, Data genera los getters y setters. noArgsConstructor un constructor sin parametros
// la gracia de esto es que inyecta el codigo, podemos varias los atributos de la clase sin tener que modificar codigo
// podria agregar más campos y lombok generara los getters y setters automaticamente

@NoArgsConstructor
public class Producto 
{ @Id
    // Anotación para generar automáticamente el valor del ID
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    // Campos privados de la clase Producto
    private String nombre;
    private String marca;
    private int precio;
    private int cantidad;

    // Constructor con todos los campos (excepto el ID)
    public Producto(String nombre, String marca, int precio, int cantidad) {
        this.nombre = nombre;
        this.marca = marca;
        this.precio = precio;
        this.cantidad = cantidad;
    }

    // Métodos getter y setter para cada campo

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public int getPrecio() {
        return precio;
    }

    public void setPrecio(int precio) {
        this.precio = precio;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
}


