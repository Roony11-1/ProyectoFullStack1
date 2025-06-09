package com.patitofeliz.sale_service.service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.patitofeliz.sale_service.model.CarritoProducto;
import com.patitofeliz.sale_service.model.Venta;
import com.patitofeliz.sale_service.model.conexion.Alerta;
import com.patitofeliz.sale_service.model.conexion.Carrito;
import com.patitofeliz.sale_service.model.conexion.Inventario;
import com.patitofeliz.sale_service.model.conexion.Producto;
import com.patitofeliz.sale_service.model.conexion.ProductoInventario;
import com.patitofeliz.sale_service.model.conexion.Sucursal;
import com.patitofeliz.sale_service.model.conexion.Usuario;
import com.patitofeliz.sale_service.repository.VentaRepository;

@Service
public class VentaService 
{
    @Autowired
    private VentaRepository ventaRepository;
    @Autowired
    private RestTemplate restTemplate;

    private static final String PRODUCTO_API = "http://localhost:8005/producto";
    private static final String USUARIO_API = "http://localhost:8001/usuario";
    private static final String ALERTA_API = "http://localhost:8002/alerta";
    private static final String CARRITO_API = "http://localhost:8003/carrito";
    private static final String INVENTARIO_API = "http://localhost:8004/inventarios";
    private static final String SUCURSAL_API = "http://localhost:8008/sucursal";

    public List<Venta> getVentasPorUsuarioId(int id)
    {
        return ventaRepository.findByUsuarioId(id);
    }

    public List<Venta> getVentasPorVendedorId(int id)
    {
        return ventaRepository.findByVendedorId(id);
    }

    public List<Venta> getVentas()
    {
        return ventaRepository.findAll();
    }

    public Venta getVentaId(int id)
    {
        return ventaRepository.findById(id).orElse(null);
    }

    public boolean existePorId(int id) 
    {
        return ventaRepository.existsById(id);
    }

    @Transactional
    public void elimiarPorId(int id)
    {
        ventaRepository.deleteById(id);
    }

    @Transactional
    public Venta generarVenta(Venta venta)
    {
        Carrito carritoVenta = getCarrito(venta.getCarritoId());
        Usuario usuario = getUsuario(venta.getUsuarioId());
        Usuario vendedor = getUsuario(venta.getVendedorId());
        Sucursal sucursal = getSucursal(venta.getSucursalId());
        Inventario inventario = getInventario(sucursal.getInventarioId());

        if (carritoVenta.getListaProductos().isEmpty())
            throw new NoSuchElementException("Carrito vacío!");


        // Creamos una Lista para guardar los producto inventario
        List<ProductoInventario> listaInventarioActualizar = new ArrayList<>();

        // Descuento de productos del inventario
        for (CarritoProducto producto : carritoVenta.getListaProductos()) 
        {
            Producto productoInventario = getProducto(producto.getProductoId());

            // Buscar producto en el inventario
            ProductoInventario productoEnInventario = null;
            for (ProductoInventario p : inventario.getListaProductos()) 
            {
                if (p.getProductoId() == producto.getProductoId()) 
                {
                    productoEnInventario = p;
                    break;
                }
            }

            if (productoEnInventario == null)
                throw new NoSuchElementException("El producto " + productoInventario.getNombre() + " no se encuentra en el inventario de la sucursal.");

            if (producto.getCantidad() > productoEnInventario.getCantidad())
                throw new NoSuchElementException("No hay suficientes existencias de " + productoInventario.getNombre());
            else
                productoEnInventario.setCantidad(productoEnInventario.getCantidad() - producto.getCantidad());

            listaInventarioActualizar.add(productoEnInventario);
        }

        descontarProductoEnInventario(sucursal.getInventarioId(), listaInventarioActualizar);

        venta.setListaProductos(carritoVenta.getListaProductos());
        venta.setTotal(carritoVenta.getTotal());

        // Borrar carrito
        restTemplate.delete(CARRITO_API + "/" + venta.getCarritoId());

        Venta nuevaVenta = ventaRepository.save(venta);

        crearAlerta(
            "Venta Carrito - id: " + venta.getCarritoId() +
            " - comprador: " + usuario.getNombreUsuario() +
            " - vendedor: " + vendedor.getNombreUsuario() +
            " - Sucursal: "+ sucursal.getNombreSucursal(),
            "Aviso: Venta"
        );

        return nuevaVenta;
    }


    // AUXILIARES
    private Usuario getUsuario(int usuarioId) 
    {
        Usuario usuario = restTemplate.getForObject(USUARIO_API + "/" + usuarioId, Usuario.class);

        if (usuario == null)
            throw new NoSuchElementException("Usuario no encontrado con ID: " + usuarioId);

        return usuario;
    }

    private Producto getProducto(int productoId) 
    {
        Producto producto = restTemplate.getForObject(PRODUCTO_API + "/" + productoId, Producto.class);

        if (producto == null)
            throw new NoSuchElementException("Producto no encontrado con ID: " + productoId);

        return producto;
    }

    private Carrito getCarrito(int carritoId) 
    {
        Carrito carrito = restTemplate.getForObject(CARRITO_API + "/" + carritoId, Carrito.class);

        if (carrito == null)
            throw new NoSuchElementException("Carrito no encontrado con ID: " + carritoId);

        return carrito;
    }

    private Inventario getInventario(int inventarioId) 
    {
        Inventario inventario = restTemplate.getForObject(INVENTARIO_API + "/" + inventarioId, Inventario.class);

        if (inventario == null)
            throw new NoSuchElementException("Inventario no encontrado con ID: " + inventarioId);

        return inventario;
    }

    private Sucursal getSucursal(int sucursalId) 
    {
        Sucursal sucursal = restTemplate.getForObject(SUCURSAL_API + "/" + sucursalId, Sucursal.class);

        if (sucursal == null)
            throw new NoSuchElementException("Sucursal no encontrada con ID: " + sucursalId);

        return sucursal;
    }

    private void descontarProductoEnInventario(int inventarioId, List<ProductoInventario> productoInventario) 
    {
        restTemplate.put(INVENTARIO_API + "/" + inventarioId + "/productos", productoInventario);
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
}
