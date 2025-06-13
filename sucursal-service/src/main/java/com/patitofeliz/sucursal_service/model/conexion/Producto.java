package com.patitofeliz.sucursal_service.model.conexion;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;



// Etiqueta de Jpa
// Etiquetas de lombook, Data genera los getters y setters. noArgsConstructor un constructor sin parametros
// la gracia de esto es que inyecta el codigo, podemos varias los atributos de la clase sin tener que modificar codigo
// podria agregar más campos y lombok generara los getters y setters automaticamente

@Data
@NoArgsConstructor
public class Producto 
{ 
    // Anotación para generar automáticamente el valor del ID
    private int id;
    
    // Campos privados de la clase Producto
    private String nombre;
    private String marca;
    private int precio;
    
}
// nashe //