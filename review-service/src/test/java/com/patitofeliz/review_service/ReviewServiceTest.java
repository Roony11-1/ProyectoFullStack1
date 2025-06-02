package com.patitofeliz.review_service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.hibernate.service.spi.InjectService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.patitofeliz.review_service.model.Review;
import com.patitofeliz.review_service.repository.ReviewRepository;
import com.patitofeliz.review_service.service.ReviewService;

public class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;

    @InjectMocks
    private ReviewService reviewService;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAll(){
        Review r1 = new Review();
        r1.setId(1);
        r1.setProductoId(1);
        r1.setUsuarioId(1);
        r1.setComentario("buen producto");
        r1.setAutor("Juanito Cliente");


        Review r2 = new Review();
        r2.setId(2);
        r2.setProductoId(2);
        r2.setUsuarioId(2);
        r2.setComentario("Mal producto");
        r2.setAutor("Elkakas Cliente");

        when(reviewRepository.findAll()).thenReturn(Arrays.asList(r1,r2));

        List<Review> resultado = reviewService.getReviews();

        assertEquals(2, resultado.size());
        assertEquals("Juanito Cliente", resultado.get(0).getAutor());
    }

    
}
