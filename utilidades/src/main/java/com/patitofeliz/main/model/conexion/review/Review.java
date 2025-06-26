package com.patitofeliz.main.model.conexion.review;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Review 
{
    private int id;
    private int productoId;
    private int usuarioId;
    private String autor;
    private String comentario;

}
