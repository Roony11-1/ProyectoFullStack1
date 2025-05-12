package review.service.review_service.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import review.service.review_service.model.Review;
import review.service.review_service.service.ReviewService;

@RestController
@RequestMapping("/review")
public class ReviewController 
{
    @Autowired
    private ReviewService reviewService;

    @GetMapping
    public ResponseEntity<List<Review>> listarReviews()
    {
        List<Review> reviews = reviewService.getReviews();

        if (reviews.isEmpty())
            return ResponseEntity.noContent().build();

        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/{productoId}")
    public ResponseEntity<Review> obtenerReview(@PathVariable("productoId") int productoId)
    {
        Review review = reviewService.getReviewByProductoId(productoId);

        if (review == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(review);
    }

    @PostMapping
    public ResponseEntity<Review> registrarReview(@RequestBody Review review)
    {
        Review reviewNueva = reviewService.registrar(review);

        return ResponseEntity.ok(reviewNueva);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Review> borrarReview(@PathVariable int id)
    {
        Review review = reviewService.getReviewByProductoId(id);

        if (review == null)
        return ResponseEntity.notFound().build();

        reviewService.borrar(id);
        return ResponseEntity.noContent().build();
    }
}
