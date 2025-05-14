package com.patitofeliz.sale_service.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.patitofeliz.sale_service.model.Carrito;
import com.patitofeliz.sale_service.model.CarritoProducto;
import com.patitofeliz.sale_service.repository.ReporitoryCarrito;

@Service
public class CarritoService 
{
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private ReporitoryCarrito carritoRepository;

    private static final String PRODUCTO_API = "http://localhost:8003/producto";

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
        Integer total = calcularTotal(carrito);

        carrito.setTotal(total);

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

    public Integer calcularTotal(Carrito carrito)
    {
        int total = 0;

        // Iteramos sobre los productos en el carrito
        for (CarritoProducto producto : carrito.getListaProductos()) 
        {
            // Obtenemos el precio del producto llamando al microservicio Producto
            Integer precioProducto = restTemplate.getForObject(PRODUCTO_API + "/precio/" + producto.getProductoId(), Integer.class);

            // Si el producto tiene precio, lo sumamos al total
            if (precioProducto != null) 
            {
                total += precioProducto * producto.getCantidad();
            }
        }

        return total;
    }

}
