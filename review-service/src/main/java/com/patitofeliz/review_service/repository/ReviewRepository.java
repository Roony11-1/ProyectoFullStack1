package com.patitofeliz.review_service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.patitofeliz.review_service.model.Review;

public interface ReviewRepository extends JpaRepository<Review, Integer> {
    List<Review> findByProductoId(Integer productoId);
}
