package com.patitofeliz.review_service.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.patitofeliz.review_service.model.Review;
import com.patitofeliz.review_service.model.conexion.Alerta;
import com.patitofeliz.review_service.model.conexion.Producto;
import com.patitofeliz.review_service.model.conexion.Usuario;
import com.patitofeliz.review_service.repository.ReviewRepository;

import jakarta.transaction.Transactional;

@Service
public class ReviewService {
   @Autowired
   private RestTemplate restTemplate;
   @Autowired
   private ReviewRepository reviewRepository;
   private static final String USUARIO_API = "http://localhost:8001/usuario";
   private static final String PRODUCTO_API="http://localhost:8004/producto";
   private static final String ALERTA_API = "http://localhost:8002/alerta";



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
   
   @Transactional
   public Review registrar(Review review)
   {
      Usuario usuario = obtenerUsuario(review.getUsuarioId());
      Producto producto = obtenerProducto(review.getProductoId());

      review.setAutor(usuario.getNombreUsuario());

      crearAlerta("Review registrada- Autor: "+review.getAutor()+" - Producto: "+producto.getNombre(), "Aviso: Review");

      Review nuevo = reviewRepository.save(review);
      return nuevo;
   }

   @Transactional
   public void borrar(int id)
   {
      reviewRepository.deleteById(id);
   }

   @Transactional
   public Review actualizar(int id, Review reviewActualizada)
   {
      Review reviewActual = reviewRepository.findById(id)
            .orElseThrow(() -> new NoSuchElementException("Review no encontrada"));

      reviewActual.setComentario(reviewActualizada.getComentario());

      return reviewRepository.save(reviewActual);
   }

   // Auxiliares

   private void crearAlerta(String mensaje, String tipoAlerta)
   {
      Alerta alertaProductoRegistrado = new Alerta(mensaje, tipoAlerta);

      try
      {
         restTemplate.postForObject(ALERTA_API, alertaProductoRegistrado, Alerta.class);
      }
      catch (RestClientException e)
      {
         throw new IllegalArgumentException("No se pudo ingresar la Alerta: "+e);
      }
   }

   private Usuario obtenerUsuario(int usuarioId) 
   {
      Usuario usuario = restTemplate.getForObject(USUARIO_API + "/" + usuarioId, Usuario.class);

      if (usuario == null)
         throw new NoSuchElementException("Usuario no encontrado con ID: " + usuarioId);

      return usuario;
   }

   private Producto obtenerProducto(int productoId) 
   {
      Producto producto = restTemplate.getForObject(PRODUCTO_API + "/" + productoId, Producto.class);

      if (producto == null)
         throw new NoSuchElementException("Producto no encontrado con ID: " + productoId);

      return producto;
   }


}
