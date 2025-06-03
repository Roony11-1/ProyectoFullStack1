package com.patitofeliz.producto_service.model.conexion;

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
