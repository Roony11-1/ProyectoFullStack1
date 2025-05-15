package com.patitofeliz.sale_service.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.patitofeliz.sale_service.model.Carrito;
import com.patitofeliz.sale_service.model.CarritoProducto;
import com.patitofeliz.sale_service.model.Venta;
import com.patitofeliz.sale_service.model.conexion.Producto;
import com.patitofeliz.sale_service.model.conexion.Usuario;
import com.patitofeliz.sale_service.repository.VentaRepository;

@Service
public class VentaService 
{
    @Autowired
    private VentaRepository ventaRepository;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private CarritoService carritoService;

    private static final String PRODUCTO_API = "http://localhost:8003/producto";
    private static final String USUARIO_API = "http://localhost:8001/usuario";


    public List<Venta> getVentasPorUsuarioId(int id)
    {
        return ventaRepository.findByUsuarioId(id);
    }

    public List<Venta> getVentasPorVendedorId(int id)
    {
        return ventaRepository.findByVendedorId(id);
    }

    public Venta getVentaId(int id)
    {
        return ventaRepository.findById(id).orElse(null);
    }

    @Transactional
    public Venta generarVenta(Venta venta)
    {
        Carrito carritoVenta = carritoService.getCarrito(venta.getCarritoId());
        Usuario usuario = restTemplate.getForObject(USUARIO_API+"/"+venta.getUsuarioId(), Usuario.class);
        Usuario vendedor = restTemplate.getForObject(USUARIO_API+"/"+venta.getVendedorId(), Usuario.class);
        
        if (carritoVenta == null)
            throw new NoSuchElementException("Carrito no encontrado");

        if (usuario == null)
            throw new NoSuchElementException("Usuario no encontrado");

        if (vendedor == null)
            throw new NoSuchElementException("Vendedor no encontrado");

        if (carritoVenta.getListaProductos().isEmpty())
            throw new NoSuchElementException("Carrito vacío!");

        // Descuento de productos del inventario
        for (CarritoProducto producto : carritoVenta.getListaProductos()) 
        {
            Producto productoInventario = restTemplate.getForObject(PRODUCTO_API+"/"+producto.getProductoId(), Producto.class);
            if (productoInventario == null)
                throw new NoSuchElementException("Producto con id "+producto.getProductoId()+" no encontrado");

            if (producto.getCantidad()>productoInventario.getCantidadInventario())
                throw new NoSuchElementException("No hay suficientes existencias de "+productoInventario.getNombre());
            else
            {
                productoInventario.setCantidadInventario(productoInventario.getCantidadInventario()-producto.getCantidad());
                restTemplate.put(PRODUCTO_API+"/update/"+producto.getProductoId(), productoInventario);
            }
        }

        venta.setListaProductos(carritoVenta.getListaProductos());
        venta.setTotal(carritoVenta.getTotal());

        carritoService.borrar(venta.getCarritoId());

        Venta nuevaVenta = ventaRepository.save(venta);

        return nuevaVenta;
    }
}
