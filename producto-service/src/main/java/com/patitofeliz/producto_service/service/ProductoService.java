package com.patitofeliz.producto_service.service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.patitofeliz.main.client.AlertaServiceClient;
import com.patitofeliz.main.client.ReviewServiceClient;
import com.patitofeliz.main.model.conexion.review.Review;
import com.patitofeliz.producto_service.model.Producto;
import com.patitofeliz.producto_service.repository.ProductoRepository;

import jakarta.transaction.Transactional;


@Service
public class ProductoService 
{
    @Autowired
    private ProductoRepository productoRepository;
    @Autowired
    private AlertaServiceClient alertaServiceClient;
    @Autowired
    private ReviewServiceClient reviewServiceClient;


    public List<Producto> getProductos()
    {
        return productoRepository.findAll();
    }

    public Producto getProducto(int id)
    {
        return productoRepository.findById(id).orElse(null);
    }

    public boolean existePorId(int id) 
    {
        return productoRepository.existsById(id);
    }

    public List<Review> getReviewsByProductoId(int id) 
    {
        List<Review> listaReseñasPorId = reviewServiceClient.getReviewsByProductoId(id);

        return listaReseñasPorId;
    }
    
    @Transactional
    public Producto registrar(Producto producto)
    {
        Producto nuevo = productoRepository.save(producto);

        alertaServiceClient.crearAlerta("Producto registrado: "+producto.getNombre(), "Aviso: Producto");

        return nuevo;
    }

    @Transactional
    public List<Producto> registrarLote(List<Producto> productos) 
    {
        List<Producto> productosRegistrados = new ArrayList<>();

        for (Producto producto : productos) 
        {
            Producto productoRegistrado = registrar(producto);

            productosRegistrados.add(productoRegistrado);
        }

        return productosRegistrados;
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
}
