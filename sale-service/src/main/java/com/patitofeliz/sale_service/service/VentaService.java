package com.patitofeliz.sale_service.service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.patitofeliz.main.client.AccountServiceClient;
import com.patitofeliz.main.client.AlertaServiceClient;
import com.patitofeliz.main.client.CarritoServiceClient;
import com.patitofeliz.main.client.InventoryServiceClient;
import com.patitofeliz.main.client.ProductoServiceClient;
import com.patitofeliz.main.client.SucursalServiceClient;
import com.patitofeliz.main.model.conexion.carrito.Carrito;
import com.patitofeliz.main.model.conexion.carrito.CarritoProducto;
import com.patitofeliz.main.model.conexion.inventario.Inventario;
import com.patitofeliz.main.model.conexion.inventario.ProductoInventario;
import com.patitofeliz.main.model.conexion.producto.Producto;
import com.patitofeliz.main.model.conexion.sucursal.Sucursal;
import com.patitofeliz.main.model.conexion.usuario.Usuario;
import com.patitofeliz.sale_service.model.Venta;
import com.patitofeliz.sale_service.model.VentaProducto;
import com.patitofeliz.sale_service.repository.VentaRepository;

@Service
public class VentaService 
{
    @Autowired
    private VentaRepository ventaRepository;
    @Autowired
    private AlertaServiceClient alertaServiceClient;
    @Autowired
    private CarritoServiceClient carritoServiceClient;
    @Autowired
    private AccountServiceClient accountServiceClient;
    @Autowired
    private ProductoServiceClient productoServiceClient;
    @Autowired
    private SucursalServiceClient sucursalServiceClient;
    @Autowired
    private InventoryServiceClient inventoryServiceClient;

    public List<Venta> getVentasPorUsuarioId(int id)
    {
        return ventaRepository.findByUsuarioId(id);
    }

    public List<Venta> getVentasPorVendedorId(int id)
    {
        return ventaRepository.findByVendedorId(id);
    }

    public List<Venta> getVentasPorSucursalId(int id)
    {
        return ventaRepository.findBySucursalId(id);
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
        Carrito carritoVenta = carritoServiceClient.getCarritoById(venta.getCarritoId());
        List<CarritoProducto> listaCarrito = carritoVenta.getListaProductos();
        Usuario usuario = accountServiceClient.getUsuario(carritoVenta.getUsuarioId());
        Usuario vendedor = accountServiceClient.getUsuario(venta.getVendedorId());
        Sucursal sucursal = sucursalServiceClient.getSucursal(carritoVenta.getSucursalId());
        Inventario inventario = inventoryServiceClient.getInventario(sucursal.getInventarioId());

        if (carritoVenta.getListaProductos().isEmpty())
            throw new NoSuchElementException("Carrito vacío!");

        // Creamos una Lista para guardar los producto inventario
        List<ProductoInventario> listaInventarioActualizar = new ArrayList<>();
        List<VentaProducto> listaProductosVenta = new ArrayList<>();

        // Descuento de productos del inventario
        for (CarritoProducto producto : listaCarrito) 
        {
            Producto productoInventario = productoServiceClient.getProducto(producto.getProductoId());
            VentaProducto productoVenta = new VentaProducto(producto.getProductoId(), producto.getProductoId());

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
            listaProductosVenta.add(productoVenta);
        }

        inventoryServiceClient.descontarProductoEnInventario(sucursal.getInventarioId(), listaInventarioActualizar);


        venta.setListaProductos(listaProductosVenta);
        venta.setTotal(carritoVenta.getTotal());
        venta.setUsuarioId(usuario.getId());
        venta.setSucursalId(sucursal.getId());

        // Borrar carrito
        carritoServiceClient.borrarCarritoPorId(carritoVenta.getId());

        Venta nuevaVenta = ventaRepository.save(venta);

        alertaServiceClient.crearAlerta(
            "Venta Carrito - id: " + venta.getCarritoId() +
            " - comprador: " + usuario.getNombreUsuario() +
            " - vendedor: " + vendedor.getNombreUsuario() +
            " - Sucursal: "+ sucursal.getNombreSucursal(),
            "Aviso: Venta"
        );

        return nuevaVenta;
    }
}
