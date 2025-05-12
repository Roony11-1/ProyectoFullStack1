package com.patitofeliz.review_service.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.patitofeliz.review_service.model.Review;

public interface ReviewRepository extends JpaRepository<Review, Integer> {
    Optional<Review> findByProductoId(Integer productoId);
}
