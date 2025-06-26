package com.patitofeliz.main.client;

import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import com.patitofeliz.main.model.conexion.producto.Producto;

@Service
public class ProductoServiceClient 
{
    @Autowired
    private RestTemplate restTemplate;

        private static final String PRODUCTO_API = "http://localhost:8005/producto";

    private Producto getProducto(int productoId) 
    {
        Producto producto = restTemplate.getForObject(PRODUCTO_API + "/" + productoId, Producto.class);

        if (producto == null)
            throw new NoSuchElementException("Producto no encontrado con ID: " + productoId);

        return producto;
    }

    public Producto obtenerProductoSeguro(int usuarioId) 
    {
        try 
        {
            return getProducto(usuarioId);
        } 
        catch (NoSuchElementException e) 
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado con ID: " + usuarioId);
        }
    }
}
