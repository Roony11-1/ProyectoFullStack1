package com.patitofeliz.review_service.controller;

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

import com.patitofeliz.review_service.model.Review;
import com.patitofeliz.review_service.service.ReviewService;

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

        if(reviews.isEmpty())
            return ResponseEntity.noContent().build();
        
        return ResponseEntity.ok(reviews);
     }   

<<<<<<< HEAD:review-service/src/main/java/review/service/review_service/controller/ReviewController.java
    @GetMapping("/{id}")
    public ResponseEntity<Review> obtenerReviewId(@PathVariable("id") int id)
    {
        Review review = reviewService.getReview(id);

        if (review == null)
            return ResponseEntity.notFound().build();

=======
     @GetMapping("/{productoId}")
     public ResponseEntity<Review> obtenerReview(@PathVariable("productoId") int productoId)
     {
        Review review = reviewService.getReviewByProductoId(productoId);
        
        if(review == null)
            return ResponseEntity.notFound().build();
        
>>>>>>> 857f9709caa4f736893dfc6774216d88ddfef2c4:review-service/src/main/java/com/patitofeliz/review_service/controller/ReviewController.java
        return ResponseEntity.ok(review);
     }

<<<<<<< HEAD:review-service/src/main/java/review/service/review_service/controller/ReviewController.java
   @GetMapping("/producto/{productoId}")
    public ResponseEntity<List<Review>> obtenerReviewPorProductoId(@PathVariable("productoId") int productoId)
    {
        List<Review> reviews = reviewService.getReviewsByProductoId(productoId);

        if (reviews == null)
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(reviews);
    }


    @PostMapping
    public ResponseEntity<Review> registrarReview(@RequestBody Review review)
    {
=======
     @PostMapping
     public ResponseEntity<Review> registrarReview(@RequestBody Review review)
     {
>>>>>>> 857f9709caa4f736893dfc6774216d88ddfef2c4:review-service/src/main/java/com/patitofeliz/review_service/controller/ReviewController.java
        Review reviewNueva = reviewService.registrar(review);

        return ResponseEntity.ok(reviewNueva);
     }

<<<<<<< HEAD:review-service/src/main/java/review/service/review_service/controller/ReviewController.java
    @DeleteMapping("/{id}")
    public ResponseEntity<Review> borrarReview(@PathVariable int id)
    {
        Review review = reviewService.getReview(id);

        if (review == null)
            return ResponseEntity.notFound().build();

=======
     @DeleteMapping("/{id}")
     public ResponseEntity<Review> borrarReview(@PathVariable int id)
     {
        Review review = reviewService.getReviewByProductoId(id);

        if (review == null)
            return ResponseEntity.notFound().build();
        
>>>>>>> 857f9709caa4f736893dfc6774216d88ddfef2c4:review-service/src/main/java/com/patitofeliz/review_service/controller/ReviewController.java
        reviewService.borrar(id);
            
        return ResponseEntity.noContent().build();
     }
    
}
