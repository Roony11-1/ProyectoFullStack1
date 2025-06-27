package com.patitofeliz.review_service.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.patitofeliz.main.client.AccountServiceClient;
import com.patitofeliz.main.client.AlertaServiceClient;
import com.patitofeliz.main.client.ProductoServiceClient;
import com.patitofeliz.main.model.conexion.producto.Producto;
import com.patitofeliz.main.model.conexion.usuario.Usuario;
import com.patitofeliz.review_service.model.Review;
import com.patitofeliz.review_service.repository.ReviewRepository;

import jakarta.transaction.Transactional;

@Service
public class ReviewService 
{
   @Autowired
   private ReviewRepository reviewRepository;
   @Autowired
   private AlertaServiceClient alertaServiceClient;
   @Autowired
   private AccountServiceClient accountServiceClient;
   @Autowired
   private ProductoServiceClient productoServiceClient;

   private static final String TIPO_AVISO = "Review";

   public List<Review> getReviews()
   {
      return reviewRepository.findAll();
   }

   public Review getReview(int id)
   {
      return reviewRepository.findById(id).orElse(null);
   }

   public List<Review> getReviewByUsuarioId(int usuarioId)
   {
      return reviewRepository.findByUsuarioId(usuarioId);
   }

   public List<Review> getReviewByProductoId(int productoId)
   {
      return reviewRepository.findByProductoId(productoId);
   }

   public boolean existePorId(int id) 
   {
      return reviewRepository.existsById(id);
   }
   
   @Transactional
   public Review registrar(Review review)
   {
      Usuario usuario = accountServiceClient.getUsuario(review.getUsuarioId());
      Producto producto = productoServiceClient.getProducto(review.getProductoId());

      review.setAutor(usuario.getNombreUsuario());

      alertaServiceClient.crearAlerta("Review registrada- Autor: "+review.getAutor()+" - Producto: "+producto.getNombre(), TIPO_AVISO);

      return reviewRepository.save(review);
   }

   @Transactional
   public void borrar(int id)
   {
      if (!existePorId(id))
         throw new NoSuchElementException("No se encontró la review con ID: " + id);

      reviewRepository.deleteById(id);
      
      alertaServiceClient.crearAlerta("Review Borrada - ID: "+id, TIPO_AVISO);
   }

   @Transactional
   public Review actualizar(int id, Review reviewActualizada)
   {
      Review reviewActual = reviewRepository.findById(id)
            .orElseThrow(() -> new NoSuchElementException("Review no encontrada"));

      reviewActual.setComentario(reviewActualizada.getComentario());

      alertaServiceClient.crearAlerta("Review actualizada- Autor: "+reviewActual.getAutor()+" - Producto: "+reviewActual.getProductoId(), TIPO_AVISO);

      return reviewRepository.save(reviewActual);
   }
}
