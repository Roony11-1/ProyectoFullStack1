package com.patitofeliz.review_service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.hibernate.service.spi.InjectService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

import com.patitofeliz.review_service.model.Review;
import com.patitofeliz.review_service.model.conexion.Producto;
import com.patitofeliz.review_service.model.conexion.Usuario;
import com.patitofeliz.review_service.repository.ReviewRepository;
import com.patitofeliz.review_service.service.ReviewService;

public class ReviewServiceTest {



    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private RestTemplate restTemplate;

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

    @Test
    public void testGetReviewById_NoExiste(){
        when(reviewRepository.findById(99)).thenReturn(Optional.empty());
        Review resultado = reviewService.getReview(99);
        assertNull(resultado);
    }

    @Test
    public void testGetReview_Existe(){
        Review r5 = new Review();
        r5.setId(1);
        r5.setAutor("JauncarloChupapija");

        when(reviewRepository.findById(1)).thenReturn(Optional.of(r5));
        Review resultado = reviewService.getReview(1);
        assertEquals(1, resultado.getId());
        assertEquals("JauncarloChupapija", resultado.getAutor());
    }


    @Test
    public void testSave(){
        Producto produ1 = new Producto();
        produ1.setId(1);
        Usuario us = new Usuario();
        us.setId(1);
        Review r3 = new Review();
        r3.setAutor("Juancho");
        r3.setUsuarioId(1);
        r3.setProductoId(1);

        when(restTemplate.getForObject(eq("http://localhost:8005/producto/1"), eq(Producto.class))).thenReturn(produ1);
        when(restTemplate.getForObject(eq("http://localhost:8001/usuario/1"), eq(Usuario.class))).thenReturn(us);
        when(reviewRepository.save(r3)).thenReturn(r3);
    
        Review resultado = reviewService.registrar(r3);
    
        assertEquals(1, resultado.getProductoId());
         
    }

    @Test
    public void testBorrar(){
        Review r4 = new Review();
        r4.setId(1);

        when(reviewRepository.findById(1)).thenReturn(Optional.of(r4));

        reviewService.borrar(1);
        
        verify(reviewRepository).deleteById(1);
    }
    


    
}
