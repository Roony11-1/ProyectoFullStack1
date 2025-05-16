package com.patitofeliz.review_service.model;


import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
public class Usuario {

    private int id;
    private int productoId;
    private int usuarioId;
    private String autor;
    private String comentario;
}
