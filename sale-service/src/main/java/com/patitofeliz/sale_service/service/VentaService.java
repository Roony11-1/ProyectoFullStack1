package com.patitofeliz.sale_service.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.patitofeliz.sale_service.model.Carrito;
import com.patitofeliz.sale_service.model.CarritoProducto;
import com.patitofeliz.sale_service.model.Venta;
import com.patitofeliz.sale_service.model.conexion.Alerta;
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
    private static final String ALERTA_API = "http://localhost:8002/alerta";


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

    private Usuario getUsuarioId(int id)
    {
        Usuario usuario = restTemplate.getForObject(USUARIO_API+"/"+id, Usuario.class);

        if (usuario == null)
            throw new NoSuchElementException("Usuario no encontrado");

        return usuario;
    }

    private Producto obtenerProductoPorId(int id) 
    {
        Producto producto = restTemplate.getForObject(PRODUCTO_API +"/"+id, Producto.class);
        if (producto == null)
            throw new NoSuchElementException("Producto con id "+id+" no encontrado");
        return producto;
    }

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

    private void actualizarProductoInventario(int id, Producto productoActualizado) 
    {
        restTemplate.put(PRODUCTO_API + "/update/" + id, productoActualizado);
    }

    @Transactional
    public Venta generarVenta(Venta venta)
    {
        Carrito carritoVenta = carritoService.getCarrito(venta.getCarritoId());
        Usuario usuario = getUsuarioId(venta.getUsuarioId());
        Usuario vendedor = getUsuarioId(venta.getVendedorId());
        
        if (carritoVenta == null)
            throw new NoSuchElementException("Carrito no encontrado");

        if (carritoVenta.getListaProductos().isEmpty())
            throw new NoSuchElementException("Carrito vacío!");

        // Descuento de productos del inventario
        for (CarritoProducto producto : carritoVenta.getListaProductos()) 
        {
            Producto productoInventario = obtenerProductoPorId(producto.getProductoId());

            if (producto.getCantidad()>productoInventario.getCantidadInventario())
                throw new NoSuchElementException("No hay suficientes existencias de "+productoInventario.getNombre());
            else
            {
                productoInventario.setCantidadInventario(productoInventario.getCantidadInventario()-producto.getCantidad());
                actualizarProductoInventario(producto.getProductoId(), productoInventario);
            }
        }

        venta.setListaProductos(carritoVenta.getListaProductos());
        venta.setTotal(carritoVenta.getTotal());

        carritoService.borrar(venta.getCarritoId());

        Venta nuevaVenta = ventaRepository.save(venta);

        crearAlerta("Venta Carrito - id: "+venta.getCarritoId()+" - comprador: "+usuario.getNombreUsuario()+" vendedor: "+vendedor.getNombreUsuario(), "Aviso: Venta");

        return nuevaVenta;
    }
}
