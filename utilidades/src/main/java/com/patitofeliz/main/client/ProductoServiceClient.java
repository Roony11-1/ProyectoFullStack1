package com.patitofeliz.main.client;

import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.patitofeliz.main.model.conexion.producto.Producto;

@Service
public class ProductoServiceClient 
{
    @Autowired
    private RestTemplate restTemplate;

        private static final String PRODUCTO_API = "http://localhost:8005/producto";

    public Producto getProducto(int productoId) 
    {
        Producto producto = restTemplate.getForObject(PRODUCTO_API + "/" + productoId, Producto.class);

        if (producto == null)
            throw new NoSuchElementException("Producto no encontrado con ID: " + productoId);

        return producto;
    }
}
