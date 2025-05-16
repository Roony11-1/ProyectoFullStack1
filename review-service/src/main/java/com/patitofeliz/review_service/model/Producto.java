package com.patitofeliz.review_service.model;


import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
public class Producto {

    private int id;
    private int productoId;
    private int usuarioId;
    private String autor;
    private String comentario;
}
