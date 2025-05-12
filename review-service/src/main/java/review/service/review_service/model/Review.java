package review.service.review_service.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
// Etiquetas de lombook, Data genera los getters y setters. noArgsConstructor un constructor sin parametros
// la gracia de esto es que inyecta el codigo, podemos varias los atributos de la clase sin tener que modificar codigo
// podria agregar más campos y lombok generara los getters y setters automaticamente
@Data
@NoArgsConstructor

public class Review 
{
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;
     // Etiquetas de Jpa, lo que le indicamos que la columna debe ser unica y no puede ser nula
    @Column(unique = true, nullable = false)
    private int productoId;
    private String autor;
    private String comentario;
    private int calificacion;

}
