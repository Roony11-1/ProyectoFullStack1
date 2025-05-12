package com.patitofeliz.sale_service.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.patitofeliz.sale_service.model.Carrito;
import com.patitofeliz.sale_service.repository.ReporitoryCarrito;

@Service
public class CarritoService 
{
    @Autowired
    private ReporitoryCarrito carritoRepository;

    public List<Carrito> getCarritos()
    {
        return carritoRepository.findAll();
    }

    public Carrito getCarrito(int id)
    {
        return carritoRepository.findById(id).orElse(null);
    }

    public Carrito findByNombre(int id) 
    {
        return carritoRepository.findByUsuarioId(id);
    }

    public Carrito guardar(Carrito carrito)
    {
        Carrito nuevo = carritoRepository.save(carrito);

        return nuevo;
    }

    public Carrito actualizar(int id, Carrito carritoActualizado)
    {
        Carrito carritoActual = carritoRepository.findById(id).orElse(null);

        if (carritoActual == null)
            throw new NoSuchElementException("Carrito no encontrado");

        carritoActual.setUsuarioId(carritoActualizado.getUsuarioId());
        //carritoActual.setListaProductos(carritoActualizado.getListaProductos());

        return carritoRepository.save(carritoActual);
    }

    public void borrar(int id)
    {
        carritoRepository.deleteById(id);
    }

}
