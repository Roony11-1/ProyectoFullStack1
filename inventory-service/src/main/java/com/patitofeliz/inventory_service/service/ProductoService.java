package com.patitofeliz.inventory_service.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.patitofeliz.inventory_service.model.Producto;
import com.patitofeliz.inventory_service.repository.ProductoRepository;

@Service
public class ProductoService 
{
    @Autowired
    private ProductoRepository productoRepository;

    public List<Producto> getProductos()
    {
        return productoRepository.findAll();
    }

    public Producto getProducto(int id)
    {
        return productoRepository.findById(id).orElse(null);
    }
 
    public Producto registrar(Producto producto)
    {
    
        Producto nuevo = productoRepository.save(producto);

        return nuevo;
    }

    public Producto actualizar(int id, Producto productoActualizado)
    {
        Producto productoActual = productoRepository.findById(id).orElse(null);

        if (productoActual == null)
            throw new NoSuchElementException("Producto no encontrado");


        productoActual.setNombre(productoActualizado.getNombre());
        productoActual.setMarca(productoActualizado.getMarca());
        productoActual.setPrecio(productoActualizado.getPrecio());
        productoActual.setCantidadInventario(productoActualizado.getCantidadInventario());

        return productoRepository.save(productoActual);
    }

    public void borrar(int id)
    {
        productoRepository.deleteById(id);
    }

    public String getMarcaProductoPorId(int id)
    {
        Producto producto = productoRepository.findById(id).orElse(null);

        if (producto == null)
            return null;

        return producto.getMarca();
    }

    public Integer getPrecioProducto(int id)
    {
        Producto producto = productoRepository.findById(id).orElse(null);

        if (producto == null)
            return null;

        return producto.getPrecio();
    }

    public String getNombreProductoPorId(int id)
    {
        Producto producto = productoRepository.findById(id).orElse(null);

        if (producto == null)
            return null;

        return producto.getNombre();
    }

    public Integer getCantidadProducto(int id)
    {
        Producto producto = productoRepository.findById(id).orElse(null);

        if (producto == null)
            return null;

        return producto.getCantidadInventario();
    }
}
