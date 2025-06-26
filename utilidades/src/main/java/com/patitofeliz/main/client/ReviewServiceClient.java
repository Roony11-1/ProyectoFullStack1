package com.patitofeliz.main.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.patitofeliz.main.model.conexion.review.Review;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ReviewServiceClient 
{
    private static final String REVIEW_API = "http://localhost:8006/review";

    @Autowired
    private RestTemplate restTemplate;

    public List<Review> getReviewsByUsuarioId(int id) 
    {
        List<Review> reviews = restTemplate.getForObject(REVIEW_API + "/usuario/" + id, List.class);

        if (reviews == null || reviews.isEmpty())
            throw new NoSuchElementException("No se encontraron reseñas para el usuario con ID: " + id);

        return reviews;
    }

    public List<Review> getReviewsByProductoId(int id) 
    {
        List<Review> reviews = restTemplate.getForObject(REVIEW_API + "/producto/" + id, List.class);

        if (reviews == null || reviews.isEmpty())
            throw new NoSuchElementException("No se encontraron reseñas para el producto con ID: " + id);

        return reviews;
    }
}
