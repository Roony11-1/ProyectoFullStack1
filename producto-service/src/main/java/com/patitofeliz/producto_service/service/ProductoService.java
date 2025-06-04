package com.patitofeliz.producto_service.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.patitofeliz.producto_service.model.Producto;
import com.patitofeliz.producto_service.model.conexion.Alerta;
import com.patitofeliz.producto_service.model.conexion.Review;
import com.patitofeliz.producto_service.repository.ProductoRepository;

import jakarta.transaction.Transactional;


@Service
public class ProductoService 
{
    @Autowired
    private ProductoRepository productoRepository;
    @Autowired
    private RestTemplate restTemplate;

    private static final String REVIEW_API = "http://localhost:8004/review";
    private static final String ALERTA_API = "http://localhost:8002/alerta";

    public List<Producto> getProductos()
    {
        return productoRepository.findAll();
    }

    public Producto getProducto(int id)
    {
        return productoRepository.findById(id).orElse(null);
    }
    
    @Transactional
    public Producto registrar(Producto producto)
    {
        Producto nuevo = productoRepository.save(producto);

        crearAlerta("Producto registrado: "+producto.getNombre(), "Aviso: Producto");

        return nuevo;
    }

    @Transactional
    public Producto actualizar(int id, Producto productoActualizado)
    {
        Producto productoActual = productoRepository.findById(id)
            .orElseThrow(() -> new NoSuchElementException("Producto no encontrado"));      

        productoActual.setNombre(productoActualizado.getNombre());
        productoActual.setMarca(productoActualizado.getMarca());
        productoActual.setPrecio(productoActualizado.getPrecio());

        return productoRepository.save(productoActual);
    }

    @Transactional
    public void borrar(int id)
    {
        productoRepository.deleteById(id);
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

   // Conexiones
   
    public List<Review> getReviewsByProductoId(int id) 
    {
        List<Review> listaReseñasPorId = restTemplate.getForObject(REVIEW_API+"/producto/"+id, List.class);

        if (listaReseñasPorId == null || listaReseñasPorId.isEmpty())
            throw new NoSuchElementException("No se encontraron reseñas para el producto con ID: " + id);

        return listaReseñasPorId;
    }
}
