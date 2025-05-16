package com.patitofeliz.review_service.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.patitofeliz.review_service.model.Review;
import com.patitofeliz.review_service.repository.ReviewRepository;

@Service
public class ReviewService {
     @Autowired
     private ReviewRepository reviewRepository;
     @Autowired
     private RestTemplate restTemplate;
    @Autowired
    private ReviewService reviewService;
    private static final String USUARIO_API = "http://localhost:8001/usuario";


     public List<Review> getReviews()
     {
        return reviewRepository.findAll();
     }

     public Review getReview(int id)
     {
         return reviewRepository.findById(id).orElse(null);
     }

     public List<Review> getReviewByProductoId(int productoId)
     {
        return reviewRepository.findByProductoId(productoId);
     }
     
     public Review registrar(Review review)
     {
        Review nuevo = reviewRepository.save(review);
        return nuevo;
     }

     public void borrar(int id)
     {
        reviewRepository.deleteById(id);
     }

     public Review actualizado(int id, Review review) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'actualizado'");
     }
     
     

}
