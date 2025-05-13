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

    @GetMapping("/{id}")
    public ResponseEntity<Review> obtenerReviewId(@PathVariable("id") int id)
    {
        Review review = reviewService.getReview(id);

        if (review == null)
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(review);
     }

   @GetMapping("/producto/{productoId}")
    public ResponseEntity<List<Review>> obtenerReviewPorProductoId(@PathVariable("productoId") int productoId)
    {
        List<Review> reviews = reviewService.getReviewByProductoId(productoId);

        if (reviews == null)
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(reviews);
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
        Review review = reviewService.getReview(id);

        if (review == null)
            return ResponseEntity.notFound().build();

        reviewService.borrar(id);
            
        return ResponseEntity.noContent().build();
     }
    
}
