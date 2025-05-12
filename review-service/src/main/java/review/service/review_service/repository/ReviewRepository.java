package review.service.review_service.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import review.service.review_service.model.Review;

public interface ReviewRepository extends JpaRepository<Review, Integer> {
    // Metodo abstracto con return de Optional, es una clase que es más comodo verificar y tratar condiciones
    // puedes preguntar si existe, y el caso que fuera nulo se maneja automaticamente
    // Ademas Jpa al seguir la estructura findBy... genera el metodo automaticamente no hay que crearlo nosotros
    // almenos se que es asi al buscar atributos (nombre, edad, correo)
    Optional<Review> findByReview(String email);

}
