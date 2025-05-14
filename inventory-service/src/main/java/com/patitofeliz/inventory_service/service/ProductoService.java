package com.patitofeliz.inventory_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.patitofeliz.inventory_service.model.Producto;
import com.patitofeliz.inventory_service.repository.ProductoRepository;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ProductoService 
{
    @Autowired
    private ProductoRepository productoRepository;

    public List<Producto> getProducto()
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
        productoActual.setCantidad(productoActualizado.getCantidad());

        return productoRepository.save(productoActual);
    }

    public void borrar(int id)
    {
        productoRepository.deleteById(id);
    }

    public List<Producto> getAll() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAll'");
    }

    public Producto getProductoById(int id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getProductoById'");
    }

    public Producto save(Producto producto) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'save'");
    }

    public List<Producto> ByUsuarioId(int id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'ByUsuarioId'");
    }
}
